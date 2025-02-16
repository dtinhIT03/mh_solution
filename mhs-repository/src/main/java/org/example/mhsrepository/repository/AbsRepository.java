package org.example.mhsrepository.repository;

import jakarta.annotation.PostConstruct;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.example.mhcommon.data.model.SearchRequest;
import org.example.mhsrepository.repository.utils.PostgresUtil;
import org.jooq.*;
import org.jooq.Record;
import org.jooq.impl.DSL;
import org.jooq.impl.TableImpl;
import org.jooq.impl.TableRecordImpl;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.ParameterizedType;
import java.util.*;
import java.util.stream.Collectors;

import static com.google.common.collect.Lists.partition;
import static org.apache.commons.lang3.StringUtils.isEmpty;

@Log4j2
public abstract class AbsRepository<R extends TableRecordImpl<R>,P,ID> implements IBaseRepository<P, ID> {
    @Autowired
    protected DSLContext dslContext;
    protected R record; //bản ghi trong 1 bảng
    private Class<P> pojoClass; //pojo là doi tuong java
    protected TableField<R, ID> fieldID; //R : kiểu bảng, ID : kiểu khóa chính
    protected abstract TableImpl<R> getTable(); //mục đích lấy tên bảng

    @SneakyThrows //bọc try cach bắn ra lỗi runtime
    @PostConstruct //hàm dưới đc gọi khi bean đc tạo và tiêm
    public void init(){
        log.info("init class {}", this.getClass().getSimpleName());

        /*
        trả về 1 object của class generic đầu tiên của lớp cha

         */
        this.record = ((Class<R>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0]) //lấy danh sách kiểu generic của class cha và chọn phần tử đầu tiên [0]
                .getDeclaredConstructor()//lấy constructor mặc định(k tham số) của class
                .newInstance();//tạo đối tượng mới từ constructor đó
        /*
         lấy danh sách kiểu generic của class cha và chọn tham số thứ 2
         */
        this.pojoClass = ((Class<P>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[1]);

        /*
        lấy trường id từ bảng
         */
        this.fieldID = (TableField<R, ID>) Arrays.stream(getTable().fields())
                .filter(field -> field.getName().equalsIgnoreCase("id"))
                .findFirst()
                .orElse(null);

    }
    @Override
    public Optional<P> insertReturning(P pojo) {
        return Optional.ofNullable(dslContext.insertInto(getTable())
                .set(PostgresUtil.toInsertQueries(record,pojo)) //chuyển Pojo thành 1 ds cặp (field,value)
                .returning()
                .fetchOneInto(pojoClass)); //chuyển thành Pojo  = new object(...)
    }


    @Override
    public Integer update(P pojo, ID id) {
        return null;
    }

    @Override
    public Integer delete(ID id) {
        return null;
    }

    @Override
    public Optional<P> findById(ID id) {
        if(fieldID == null){
            return null;
        }

        return Optional.ofNullable(dslContext.selectFrom(getTable())
                .where(fieldID.eq(id))
                .fetchOneInto(pojoClass));
    }

    @Override
    public List<Integer> insert(Collection<P> pojos) {
        final List<InsertSetMoreStep<R>> insertSetMoreSteps = pojos.stream()
                .map(p -> PostgresUtil.toInsertQueries(record,p))// chuyển Pojo thành dữ liệu cột
                .map(fieldObjectMap -> dslContext
                        .insertInto(getTable())//tạo câu insert into table
                        .set(fieldObjectMap))
                .collect(Collectors.toList());
        return partition(insertSetMoreSteps, 100)
                .stream()
                .flatMap(lists -> Arrays.stream(dslContext.batch(lists).execute()).boxed())
                .collect(Collectors.toList());
    }

    @Override
    public List<P> getAll() {
        return dslContext.select()
                .from(getTable())
                .fetchInto(pojoClass);
    }

    @Override
    public List<P> search(SearchRequest searchRequest) {
        return dslContext.selectFrom(getTable())
                .where(PostgresUtil.buildFilterQueries(getTable(),searchRequest.getFilters())
                        .and(filterActive())
                        .and(buildSearchQueries(getTable(),searchRequest.getKeyword())))
                .orderBy(PostgresUtil.toSortField(searchRequest.getSorts(),getTable().fields()))
                .limit(searchRequest.getPageSize())
                .offset(searchRequest.getOffset())
                .fetchInto(pojoClass);
    }

    //xoá mềm
    protected Condition filterActive(){
        //check co truong deleted_at ?
        Field<?> deletedAt = getTable().field("deleted_at");
        if(deletedAt != null) {
            return deletedAt.isNull();
        }
        return DSL.trueCondition();
    }


    //xây dựng điều kiện tìm kiếm dựa trên từ khóa
    public static <R extends Record> Condition buildSearchQueries(TableImpl<R> table,String keyword) {
        if(isEmpty(keyword)) return DSL.noCondition();
        final Condition[] condition = {DSL.noCondition()};
        Arrays.stream(table.fields())
                .filter(field -> String.class.isAssignableFrom(field.getType()) || Long.class.isAssignableFrom(field.getType()))
                .forEach(field -> condition[0] = condition[0].or(field.cast(String.class).likeRegex(keyword)));
        return condition[0];
    }
    public Long count(SearchRequest searchRequest){
        return dslContext
                .selectCount()
                .from(getTable())
                .where(PostgresUtil.buildFilterQueries(getTable(),searchRequest.getFilters())
                        .and(filterActive())
                        .and(buildSearchQueries(getTable(),searchRequest.getKeyword())))
                .fetchOneInto(Long.class);
    }
}

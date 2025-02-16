package org.example.mhsrepository.repository.utils;


import org.example.mhcommon.core.json.JsonArray;
import org.example.mhcommon.core.json.JsonObject;
import org.example.mhcommon.data.model.Filter;
import org.example.mhcommon.data.model.paging.Order;
import org.example.mhcommon.data.model.query.Operator;
import org.example.mhcommon.utils.TimeUtils;
import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.SortField;
import org.jooq.impl.DSL;
import org.jooq.impl.TableImpl;
import org.jooq.impl.TableRecordImpl;
import org.springframework.util.CollectionUtils;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import static org.apache.commons.collections4.CollectionUtils.isEmpty;
import static org.jooq.impl.DSL.trueCondition;
import static org.apache.commons.lang3.math.NumberUtils.createLong;


public class PostgresUtil {

    public static <T extends TableRecordImpl<T>> Map<Field<?>, Object>
    toInsertQueries(T record, Object o){
        record.from(o);
        return Arrays.stream(record.fields())
                .filter(f -> record.getValue(f) != null)
                .collect(Collectors.toMap(field -> field,record::getValue));

    }

    public static <R extends Record> Condition buildFilterQueries(TableImpl<R> table, List<Filter> fieldFilters) {
        if(isEmpty(fieldFilters)) return DSL.noCondition();
        final Condition[] condition = {DSL.noCondition()};
        fieldFilters
                .forEach(fieldFilter -> {
                    final Field field = table.field(fieldFilter.getName()); // lấy cột tương ứng trong bảng
                    if (field != null) {
                        //chuyển đổi giá trị sang kiểu dữ liệu phù hợp
                        final Object valueByClass = castValueByClass(fieldFilter.getOperation(), fieldFilter.getValue(), field.getType());
                        if (valueByClass != null) {
                            //xây dựng điều kiện tìm kiếm với toán tử tương ứng
                            condition[0] = condition[0].and(buildCondition(fieldFilter.getOperation(), field, valueByClass));

                        } else {
                            condition[0] = condition[0].and(field.isNull()); // nếu giá trị null, lọc theo điều kiện `IS NULL`
                        }
                    }
                });
        return condition[0]; // trả về điều kiện tổng hợp
    }

    //chuyển toán tu EQUAL,LIKE,IN thành điều kiện SQL
    public static Condition buildCondition(String operation,Field<Object> field, Object value){
        var operator = Operator.from(operation);// lay toan tu tuong ung tu chuoi operation
        switch (operator) {
            case IN: {
                return field.in(value);
            }
            case NIN: {
                return field.notIn(value);
            }
            case EQUAL: {
                return field.eq(value);
            }
            case LIKE: {
                if (value == null) return trueCondition();
                return field.likeRegex(value.toString());
            }
            case LIKE_IGNORE: {
                if (value == null) return trueCondition();
                return field.likeIgnoreCase("%" + value.toString() + "%");
            }
            case NOT_EQUAL: {
                return field.ne(value);
            }
            case GREATER_THAN: {
                return field.gt(value);
            }
            case LESS_THAN: {
                return field.lt(value);
            }
            case GREATER_THAN_OR_EQUAL: {
                return field.greaterOrEqual(value);
            }
            case LESS_THAN_OR_EQUAL: {
                return field.lessOrEqual(value);
            }
            default:
                return field.eq(value);
        }
    }

    //chuyen doi gia tri sang kieu du lieu cua cot de tranh loi so sanh kieu du lieu trong sql
    private static <V> Object castValueByClass(String operation, Object value, Class<V> classValue){
        //enum la toan tu In hoac NOT IN -> chuyen chuoi JSON thanh danh sach
        if(operation != null && Arrays.asList(Operator.IN.getOperator(),Operator.NIN.getOperator()).contains(operation)){
            //chuyen doi value thanh chuoi json sau do chuyen thanh json array
            final JsonArray array = new JsonArray(value.toString());
            return array.stream()
                    .map(object -> castValueByClass(null,object,classValue)) //ep kieu tung phan tu object trong ds ve kieu classValue
                    .collect(Collectors.toList());
        }
        try {
            //neu la LocalDateTime
            if(classValue.getSimpleName().equalsIgnoreCase(LocalDateTime.class.getSimpleName())){
                return TimeUtils.longtoLocalDatetime(createLong(value.toString()));
            }
            if(classValue.getSimpleName().equalsIgnoreCase(Timestamp.class.getSimpleName())){
                return new Timestamp(createLong(value.toString()));
            }
            if(String.class.isAssignableFrom(classValue)) return value;
            if(Integer.class.isAssignableFrom(classValue)) return Integer.valueOf(value.toString());
            if(Double.class.isAssignableFrom(classValue)) return Double.valueOf(value.toString());
            //neu la kieu Object JSON , chuyen doi sang doi tuong tuong ung
            return JsonObject.mapFrom(value).mapTo(classValue);
        } catch (Exception e){
            return null;
        }
    }
    //chuyển danh sách thuộc tính sắp xếp  thành ds SortField để sử dụng trong câu truy vấn
    public static List<SortField<Object>> toSortField(List<Order> orderProperties, Field<?>[] fields){
        if(CollectionUtils.isEmpty(orderProperties)) return new ArrayList<>();
        //lấy danh sách tên các cột có thể sắp xếp
        Set<String> fieldNames = Arrays.stream(fields)
                .map(Field::getName)
                .collect(Collectors.toSet());
        //chuyển danh sách thành orderProperties thành SortField
        return orderProperties
                .stream()
                .filter(order -> fieldNames.contains(order.getProperty())) //chỉ lấy thuộc tính hợp lệ
                .map(order -> {
                    if(order.getDirection().equals(Order.Direction.asc.name()))
                        return DSL.field(order.getProperty()).asc();
                    else return DSL.field(order.getProperty()).desc();
                }).collect(Collectors.toList());
    }
}

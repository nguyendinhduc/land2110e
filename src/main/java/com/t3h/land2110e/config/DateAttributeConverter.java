//package com.t3h.land2110e.config;
//
//import org.springframework.stereotype.Component;
//
//import javax.persistence.AttributeConverter;
//import javax.persistence.Converter;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//
//@Converter(autoApply = true)
//@Component
//public class DateAttributeConverter implements AttributeConverter<Date, String> {
//    @Override
//    public String convertToDatabaseColumn(Date date) {
//        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(date);
//    }
//
//    @Override
//    public Date convertToEntityAttribute(String s) {
//        try {
//            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse(s);
//        } catch (ParseException e) {
//            return null;
//        }
//    }
//
//
//}

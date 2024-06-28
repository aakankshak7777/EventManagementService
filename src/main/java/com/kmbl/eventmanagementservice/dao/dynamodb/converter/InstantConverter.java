package com.kmbl.eventmanagementservice.dao.dynamodb.converter;

import java.math.BigDecimal;
import java.time.Instant;
import software.amazon.awssdk.annotations.Immutable;
import software.amazon.awssdk.annotations.ThreadSafe;
import software.amazon.awssdk.enhanced.dynamodb.AttributeConverter;
import software.amazon.awssdk.enhanced.dynamodb.AttributeValueType;
import software.amazon.awssdk.enhanced.dynamodb.EnhancedType;
import software.amazon.awssdk.enhanced.dynamodb.internal.converter.StringConverter;
import software.amazon.awssdk.enhanced.dynamodb.internal.converter.TypeConvertingVisitor;
import software.amazon.awssdk.enhanced.dynamodb.internal.converter.attribute.EnhancedAttributeValue;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

@ThreadSafe
@Immutable
public final class InstantConverter implements AttributeConverter<Instant> {

    private static final Visitor VISITOR = new Visitor();
    private static final InstantStringConverter STRING_CONVERTER = InstantStringConverter.create();

    @Override
    public EnhancedType<Instant> type() {
        return EnhancedType.of(Instant.class);
    }

    @Override
    public AttributeValueType attributeValueType() {
        return AttributeValueType.N;
    }

    @Override
    public AttributeValue transformFrom(Instant input) {
        return AttributeValue.builder().n(STRING_CONVERTER.toString(input)).build();
    }

    @Override
    public Instant transformTo(AttributeValue input) {
        if (input.n() != null) {
            return EnhancedAttributeValue.fromNumber(input.n()).convert(VISITOR);
        }
        return EnhancedAttributeValue.fromAttributeValue(input).convert(VISITOR);
    }

    private static final class Visitor extends TypeConvertingVisitor<Instant> {

        private Visitor() {
            super(BigDecimal.class, InstantConverter.class);
        }

        @Override
        public Instant convertString(String value) {
            return STRING_CONVERTER.fromString(value);
        }

        @Override
        public Instant convertNumber(String value) {
            return STRING_CONVERTER.fromString(value);
        }
    }

    @ThreadSafe
    @Immutable
    public static class InstantStringConverter implements StringConverter<Instant> {

        private InstantStringConverter() {}

        public static InstantStringConverter create() {
            return new InstantStringConverter();
        }

        @Override
        public EnhancedType<Instant> type() {
            return EnhancedType.of(Instant.class);
        }

        @Override
        public String toString(Instant input) {
            return Long.toString(input.toEpochMilli());
        }

        @Override
        public Instant fromString(String string) {
            return Instant.ofEpochMilli(Long.parseLong(string));
        }
    }
}
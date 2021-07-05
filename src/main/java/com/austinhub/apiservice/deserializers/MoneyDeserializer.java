package com.austinhub.apiservice.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.deser.std.NumberDeserializers;
import com.fasterxml.jackson.databind.deser.std.NumberDeserializers.BigDecimalDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class MoneyDeserializer extends StdDeserializer<BigDecimal> {

    public MoneyDeserializer(JavaType valueType,
            BigDecimalDeserializer delegate) {
        super(valueType);
        this.delegate = delegate;
    }

    private NumberDeserializers.BigDecimalDeserializer delegate =
            NumberDeserializers.BigDecimalDeserializer.instance;

    public MoneyDeserializer() {
        super(BigDecimal.class);
    }

    public MoneyDeserializer(Class<?> vc) {
        super(vc);
    }

    public MoneyDeserializer(StdDeserializer<?> src) {
        super(src);
    }

    @Override
    public BigDecimal deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        BigDecimal bd = delegate.deserialize(jp, ctxt);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd;
    }
}

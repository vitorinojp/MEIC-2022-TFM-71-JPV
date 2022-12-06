package org.iotmapper.processing.streams;

import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.Transformer;
import org.apache.kafka.streams.kstream.TransformerSupplier;
import org.apache.kafka.streams.processor.ProcessorContext;

import java.nio.charset.StandardCharsets;

public class AddHeadersTransformerSupplier implements TransformerSupplier<String, String, KeyValue<String, String>> {
    public String contentType, fiwareService, fiwareServicePath;

    public AddHeadersTransformerSupplier(String contentType, String fiwareService, String fiwareServicePath) {
        this.contentType = contentType;
        this.fiwareService = fiwareService;
        this.fiwareServicePath = fiwareServicePath;
    }

    @Override
    public Transformer<String, String, KeyValue<String, String>> get() {
        return new AddHeadersTransformer(contentType, fiwareService, fiwareServicePath);
    }
}

class AddHeadersTransformer implements Transformer<String, String, KeyValue<String, String>> {
    private String contentType, fiwareService, fiwareServicePath;
    private ProcessorContext context;

    AddHeadersTransformer(String contentType, String fiwareService, String fiwareServicePath) {
        this.contentType = contentType;
        this.fiwareService = fiwareService;
        this.fiwareServicePath = fiwareServicePath;
    }

    @Override
    public void init(ProcessorContext processorContext) {
        this.context = processorContext;
    }

    @Override
    public KeyValue<String, String> transform(String s, String s2) {
        context.headers().add("Content-Type", contentType.getBytes(StandardCharsets.UTF_8));
        context.headers().add("fiware-service", fiwareService.getBytes(StandardCharsets.UTF_8));
        context.headers().add("fiware-servicepath", fiwareServicePath.getBytes(StandardCharsets.UTF_8));

        return new KeyValue<>(s, s2);
    }

    @Override
    public void close() {

    }
}

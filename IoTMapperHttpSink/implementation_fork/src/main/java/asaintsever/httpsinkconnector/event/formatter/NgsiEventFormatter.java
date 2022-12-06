package asaintsever.httpsinkconnector.event.formatter;

import asaintsever.httpsinkconnector.utils.Pair;
import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.connect.header.Header;
import org.apache.kafka.connect.sink.SinkRecord;

import java.io.IOException;
import java.util.*;

public class NgsiEventFormatter implements IEventFormatter{

    @Override
    public ConfigDef configDef() {
        return new ConfigDef();
    }

    @Override
    public void configure(Map<String, ?> configs) {
    }

    @Override
    public EventBatch formatEventBatch(List<SinkRecord> batch) throws IOException {
        String events = "{\"actionType\":\"append\", \"entities\": [";
        HashMap<String, Pair<String, String>> headers = new HashMap<>();
        boolean first = true;

        for(SinkRecord record : batch) {
            Object recordValueObj = record != null ? record.value() : null;

            if(recordValueObj == null) {
                continue;
            }

            //==
            // The sink record value's type depends on the Kafka Connect connector configuration ('value.converter' param):
            // if set to 'org.apache.kafka.connect.json.JsonConverter'      => Kafka Connect will deal with JSON deserialization and we directly have an object
            // if set to 'io.confluent.connect.avro.AvroConverter'          => Kafka Connect will deal with AVRO deserialization and we directly have an object
            // if set to 'org.apache.kafka.connect.storage.StringConverter' => Kafka Connect does "nothing" and we get the raw string. We have to handle deserialization ourself in the connector (if needed)
            //==

            // Here we expect to receive values we can convert into strings
            String recordValue = recordValueObj.toString();
            if(first){
                first = false;
            }
            else {
                events = events + ",";
            }
            events = events + recordValue;

            for (Header header : record.headers()){
                String value = null;
                try{
                    value = String.valueOf(header.value());
                }catch (Exception e){
                    System.err.println(e.getMessage());
                }
                if (value != null ){
                    headers.put(header.key(), new Pair<String, String>(header.key(), value));
                }
            }
        }

        if(events.isEmpty()) {
            throw new IOException("List of events to send to HTTP endpoint is empty!");
        }

        events = events + "]}";

        return new EventBatch(events.getBytes(), new ArrayList<>(headers.values()));
    }
}

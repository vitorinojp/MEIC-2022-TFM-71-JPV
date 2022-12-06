package asaintsever.httpsinkconnector.event.formatter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import asaintsever.httpsinkconnector.utils.Pair;
import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.connect.header.Header;
import org.apache.kafka.connect.sink.SinkRecord;
import org.apache.kafka.connect.storage.StringConverter;

/*
* Default formatter implementation: expects and forward raw strings, performs no transformations
*/
public class PassthroughStringEventFormatter implements IEventFormatter {   

    @Override
    public ConfigDef configDef() {
        return new ConfigDef();
    }
    
    @Override
    public void configure(Map<String, ?> configs) {
    }
    
    @Override
    public EventBatch formatEventBatch(List<SinkRecord> batch) throws IOException {
        List<String> events = new ArrayList<>();
        List<Pair<String, String>> headers = new ArrayList<>();
        
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
            events.add(recordValue);

           for (Header header : record.headers()){
               String value = null;
               try{
                   value = String.valueOf(record.value());
               }catch (Exception e){
                   System.err.println(e.getMessage());
               }
               if (value != null)
                headers.add(new Pair<String, String>(header.key(), value));
           }
        }
        
        if(events.isEmpty()) {
            throw new IOException("List of events to send to HTTP endpoint is empty!");
        }


        
        return new EventBatch(events.toString().getBytes(), headers);
    }
    
}

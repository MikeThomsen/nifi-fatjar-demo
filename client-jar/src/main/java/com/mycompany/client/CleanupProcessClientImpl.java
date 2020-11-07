package com.mycompany.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class CleanupProcessClientImpl implements CleanupProcessClient {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    public static final String DATE_KEY = "order_date";

    /**
     * The implementation will convert a non-standard date into an ISO8601 string.
     * 
     * @param is InputStream opened by ProcessSession
     * @return
     */
    @Override
    public byte[] stripInvalidDates(InputStream is) {
        DateFormat inputDate = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        DateFormat outputDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

        try {
            String raw = IOUtils.toString(new InputStreamReader(is));
            Map<String, Object> record = MAPPER.readValue(raw, Map.class);

            Date input = inputDate.parse((String) record.get(DATE_KEY));
            String output = outputDate.format(input);

            record.put(DATE_KEY, output);

            return MAPPER.writerWithDefaultPrettyPrinter().writeValueAsBytes(record);
        } catch (ParseException | IOException e) {
            return null;
        }
    }
}

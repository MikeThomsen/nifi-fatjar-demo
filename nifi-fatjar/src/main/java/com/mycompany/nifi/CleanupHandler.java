package com.mycompany.nifi;

import com.mycompany.client.CleanupFailedException;
import com.mycompany.client.CleanupProcessClient;
import org.apache.nifi.flowfile.FlowFile;
import org.apache.nifi.logging.ComponentLog;
import org.apache.nifi.processor.ProcessSession;
import org.apache.nifi.processor.exception.ProcessException;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Wrapper class that is called from ExecuteScript
 */
public class CleanupHandler {
    private ComponentLog log;
    private CleanupProcessClient client;
    private ProcessSession session;

    /**
     *
     * @param log Logging system support. This is called "log" in the ExecuteScript global variables.
     * @param client Client that we will pass in from the script.
     * @param session The "session" variable in ExecuteScript
     */
    public CleanupHandler(ComponentLog log, CleanupProcessClient client, ProcessSession session) {
        this.log = log;
        this.client = client;
        this.session = session;
    }

    /**
     * Execute a cleanup operation on a flowfile.
     *
     * @param flowFile
     * @throws CleanupFailedException
     */
    void execute(FlowFile flowFile) throws CleanupFailedException {
        byte[] result = null;

        try (InputStream is = session.read(flowFile)) {
            result = client.stripInvalidDates(is);
        } catch (Exception ex) {
            throw new CleanupFailedException(ex);
        } finally {
            if (result != null) {
                try (OutputStream os = session.write(flowFile)) {
                    os.write(result);
                } catch (Exception ex) {
                    throw new ProcessException(ex);
                }
            }
        }
    }
}
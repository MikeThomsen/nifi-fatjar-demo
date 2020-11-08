import com.mycompany.client.CleanupFailedException
import com.mycompany.nifi.*
import com.mycompany.client.CleanupProcessClientImpl

def ffs = session.get(100)
def handler = new CleanupHandler(log, new CleanupProcessClientImpl(), session)
ffs.each { flowFile ->
    try {
        handler.execute(flowFile)
        session.transfer(flowFile, REL_SUCCESS)
    } catch (CleanupFailedException e) {
        log.error("", e)
        session.transfer(flowFile, REL_FAILURE)
    }
}
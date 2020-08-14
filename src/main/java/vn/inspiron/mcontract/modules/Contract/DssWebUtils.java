package vn.inspiron.mcontract.modules.Contract;

import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.model.InMemoryDocument;
import eu.europa.esig.dss.validation.timestamp.TimestampToken;
import eu.europa.esig.dss.ws.dto.TimestampDTO;
import eu.europa.esig.dss.ws.signature.common.TimestampTokenConverter;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public final class DssWebUtils {

    public static DSSDocument toDSSDocument(Resource resource) {
        try {
            File file = resource.getFile();
            byte[] bytes = new byte[(int) file.length()];
            FileInputStream fis = new FileInputStream(file);
            fis.read(bytes);
            fis.close();

            return new InMemoryDocument(bytes, resource.getFilename());
        } catch (IOException e) {
            System.err.println("Cannot read file : " + e.getMessage());
        }
        return null;
    }

    public static TimestampToken toTimestampToken(TimestampDTO dto) {
        return TimestampTokenConverter.toTimestampToken(dto);
    }

}

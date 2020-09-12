package vn.inspiron.mcontract.modules.Contract.config;

import eu.europa.esig.dss.service.http.commons.TimestampDataLoader;
import eu.europa.esig.dss.service.tsp.OnlineTSPSource;
import eu.europa.esig.dss.spi.x509.tsp.TSPSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TspBeanConfig {

    @Bean
    public TSPSource tspSource() throws Exception {
        final String tspServer = "http://dss.nowina.lu/pki-factory/tsa/good-tsa";
        OnlineTSPSource tspSource = new OnlineTSPSource(tspServer);
        tspSource.setDataLoader(new TimestampDataLoader());
        return tspSource;
    }
}

package vn.inspiron.mcontract.modules.Contract.config;

import eu.europa.esig.dss.alert.ExceptionOnStatusAlert;
import eu.europa.esig.dss.model.DSSException;
import eu.europa.esig.dss.model.x509.CertificateToken;
import eu.europa.esig.dss.pades.signature.PAdESService;
import eu.europa.esig.dss.service.crl.JdbcCacheCRLSource;
import eu.europa.esig.dss.service.crl.OnlineCRLSource;
import eu.europa.esig.dss.service.http.commons.CommonsDataLoader;
import eu.europa.esig.dss.service.http.commons.OCSPDataLoader;
import eu.europa.esig.dss.service.http.proxy.ProxyConfig;
import eu.europa.esig.dss.service.ocsp.JdbcCacheOCSPSource;
import eu.europa.esig.dss.service.ocsp.OnlineOCSPSource;
import eu.europa.esig.dss.spi.tsl.TrustedListsCertificateSource;
import eu.europa.esig.dss.spi.x509.CommonTrustedCertificateSource;
import eu.europa.esig.dss.spi.x509.KeyStoreCertificateSource;
import eu.europa.esig.dss.spi.x509.tsp.TSPSource;
import eu.europa.esig.dss.tsl.function.OfficialJournalSchemeInformationURI;
import eu.europa.esig.dss.tsl.source.LOTLSource;
import eu.europa.esig.dss.validation.CertificateVerifier;
import eu.europa.esig.dss.validation.CommonCertificateVerifier;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

@Configuration
@ComponentScan(basePackages = { "vn.inspiron.mcontract.modules.Contract.services" })
public class DssBeanConfig {

    // can be null
    @Autowired(required = false)
    private ProxyConfig proxyConfig;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private TSPSource tspSource;

    @Bean
    public CommonsDataLoader dataLoader() {
        CommonsDataLoader dataLoader = new CommonsDataLoader();
        dataLoader.setProxyConfig(proxyConfig);
        return dataLoader;
    }

    @Bean
    public OnlineCRLSource onlineCRLSource() {
        OnlineCRLSource onlineCRLSource = new OnlineCRLSource();
        onlineCRLSource.setDataLoader(dataLoader());
        return onlineCRLSource;
    }

    @Bean
    public JdbcCacheOCSPSource cachedOCSPSource() {
        JdbcCacheOCSPSource jdbcCacheOCSPSource = new JdbcCacheOCSPSource();
        jdbcCacheOCSPSource.setDataSource(dataSource);
        jdbcCacheOCSPSource.setProxySource(onlineOcspSource());
        jdbcCacheOCSPSource.setDefaultNextUpdateDelay((long) (1000 * 60 * 3)); // 3 minutes
        return jdbcCacheOCSPSource;
    }

    @Bean
    public JdbcCacheCRLSource cachedCRLSource() {
        JdbcCacheCRLSource jdbcCacheCRLSource = new JdbcCacheCRLSource();
        jdbcCacheCRLSource.setDataSource(dataSource);
        jdbcCacheCRLSource.setProxySource(onlineCRLSource());
        jdbcCacheCRLSource.setDefaultNextUpdateDelay((long) (60 * 3)); // 3 minutes
        return jdbcCacheCRLSource;
    }

    @Bean
    public OnlineOCSPSource onlineOcspSource() {
        OnlineOCSPSource onlineOCSPSource = new OnlineOCSPSource();
        onlineOCSPSource.setDataLoader(ocspDataLoader());
        return onlineOCSPSource;
    }

    @Bean
    public OCSPDataLoader ocspDataLoader() {
        OCSPDataLoader ocspDataLoader = new OCSPDataLoader();
        ocspDataLoader.setProxyConfig(proxyConfig);
        return ocspDataLoader;
    }

    @Bean
    public CertificateVerifier certificateVerifier() throws Exception {
        CommonCertificateVerifier certificateVerifier = new CommonCertificateVerifier();
        certificateVerifier.setCrlSource(cachedCRLSource());
        certificateVerifier.setOcspSource(cachedOCSPSource());
        certificateVerifier.setDataLoader(dataLoader());

        ClassLoader classLoader = getClass().getClassLoader();
        File certFile = new File(classLoader.getResource("tsa-root-ca.pem").getFile());
        CertificateFactory factory = CertificateFactory.getInstance("X.509");
        X509Certificate certificate = (X509Certificate) factory.generateCertificate(FileUtils.openInputStream(certFile));

        CommonTrustedCertificateSource commonTrustedCertificateSource = new CommonTrustedCertificateSource();
        commonTrustedCertificateSource.addCertificate(new CertificateToken(certificate));

//        certFile = new File(classLoader.getResource("self-signed.crt").getFile());
//        certificate = (X509Certificate) factory.generateCertificate(FileUtils.openInputStream(certFile));
//        commonTrustedCertificateSource.addCertificate(new CertificateToken(certificate));
//
        certificateVerifier.setTrustedCertSources(commonTrustedCertificateSource);

        // Default configs
        certificateVerifier.setAlertOnMissingRevocationData(new ExceptionOnStatusAlert());
        certificateVerifier.setCheckRevocationForUntrustedChains(false);

        return certificateVerifier;
    }

    @Bean
    public PAdESService pAdESService() throws Exception {
        PAdESService service = new PAdESService(certificateVerifier());
        service.setTspSource(tspSource);
        return service;
    }

}

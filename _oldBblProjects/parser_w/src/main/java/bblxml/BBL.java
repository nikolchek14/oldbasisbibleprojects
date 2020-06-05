package bblxml;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "pdf2xml") public class BBL {
        List<BBLPage> page;
        private String producer;
        private String version;

        public String getVersion() {
                return version;
        }

        public String getProducer() {
                return producer;
        }

        public List<BBLPage> getPage() {
                return page;
        }

        @XmlElement public void setPage(List<BBLPage> page) {
                this.page = page;
        }

        @XmlAttribute public void setProducer(String producer) {
                this.producer = producer;
        }

        @XmlAttribute public void setVersion(String version) {
                this.version = version;
        }

        @Override public String toString() {
                return page.toString() + producer + version;
        }
}

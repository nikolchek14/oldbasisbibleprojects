package bblxml;

import javax.xml.bind.annotation.XmlAttribute;

public class Fontspec {
        private String id;
        private String size;
        private String family;
        private String color;

        public String getId() {
                return id;
        }
        @XmlAttribute
        public void setId(String id) {
                this.id = id;
        }

        public String getSize() {
                return size;
        }
        @XmlAttribute
        public void setSize(String size) {
                this.size = size;
        }

        public String getFamily() {
                return family;
        }
        @XmlAttribute
        public void setFamily(String family) {
                this.family = family;
        }

        public String getColor() {
                return color;
        }
        @XmlAttribute
        public void setColor(String color) {
                this.color = color;
        }

        @Override
        public String toString() {
                return "Fontspec{" +
                        "id='" + id + '\'' +
                        ", size='" + size + '\'' +
                        ", family='" + family + '\'' +
                        ", color='" + color + '\'' +
                        '}';
        }
}

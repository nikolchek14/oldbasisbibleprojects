import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;

public class BBLPage {
        private String number;
        private String position;
        private String top;
        private String left;
        private String height;
        private String width;
        private List<Fontspec> fontspec;
        private List<BBLText> text;

        public String getNumber() {
                return number;
        }

        @XmlAttribute
        public void setNumber(String number) {
                this.number = number;
        }

        public String getPosition() {
                return position;
        }

        @XmlAttribute
        public void setPosition(String position) {
                this.position = position;
        }

        public String getTop() {
                return top;
        }

        @XmlAttribute
        public void setTop(String top) {
                this.top = top;
        }

        public String getLeft() {
                return left;
        }

        @XmlAttribute
        public void setLeft(String left) {
                this.left = left;
        }

        public String getHeight() {
                return height;
        }

        @XmlAttribute
        public void setHeight(String height) {
                this.height = height;
        }

        public String getWidth() {
                return width;
        }

        @XmlAttribute
        public void setWidth(String width) {
                this.width = width;
        }

        public List<Fontspec> getFontspec() {
                return fontspec;
        }

        @XmlElement
        public void setFontspec(List<Fontspec> fontspec) {
                this.fontspec = fontspec;
        }

        public List<BBLText> getText() {
                return text;
        }

        @XmlElement
        public void setText(List<BBLText> text) {
                this.text = text;
        }

        @Override public String toString() {
                return this.number;
        }
}

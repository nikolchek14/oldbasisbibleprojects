package bblxml;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;
import java.util.Objects;

public class BBLText {
        private int top;
        private int left;
        private int width;
        private int height;
        private int font;
        private int diff;
        private String value;

        public int getTop() {
                return top;
        }
        @XmlAttribute
        public void setTop(int top) {
                this.top = top;
        }

        public int getLeft() {
                return left;
        }
        @XmlAttribute
        public void setLeft(int left) {
                this.left = left;
        }

        public int getWidth() {
                return width;
        }
        @XmlAttribute
        public void setWidth(int width) {
                this.width = width;
        }

        public int getHeight() {
                return height;
        }
        @XmlAttribute
        public void setHeight(int height) {
                this.height = height;
        }

        public int getFont() {
                return font;
        }
        @XmlAttribute
        public void setFont(int font) {
                this.font = font;
        }
        public String getValue() {
                return value;
        }
        @XmlValue
        public void setValue(String value) {
                this.value = value;
        }

        public int getDiff() {
                return diff;
        }
        @XmlAttribute
        public void setDiff(int diff) {
                this.diff = diff;
        }
        @Override
        public String toString() {
                return "BBLText{" +
                        "top='" + top + '\'' +
                        ", left='" + left + '\'' +
                        ", width='" + width + '\'' +
                        ", height='" + height + '\'' +
                        ", font='" + font + '\'' +
                        ", value='" + value + '\'' +
                        ", diff='" + diff + '\'' +
                        '}';
        }

        @Override
        public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;
                BBLText bblText = (BBLText) o;
                return top == bblText.top &&
                        left == bblText.left &&
                        width == bblText.width &&
                        height == bblText.height &&
                        font == bblText.font &&
                        diff == bblText.diff &&
                        Objects.equals(value, bblText.value);
        }

        @Override
        public int hashCode() {
                return Objects.hash(top, left, width, height, font, diff, value);
        }
}

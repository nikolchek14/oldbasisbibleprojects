import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class Parser {
    public static void main(String[] args) {

        ObjectMapper mapper = new ObjectMapper();

        try {
            // JSON file to Java object
            BBL bbl = mapper.readValue(new File("src/main/resources/Bbl.json"), BBL.class);
            System.out.println(bbl);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

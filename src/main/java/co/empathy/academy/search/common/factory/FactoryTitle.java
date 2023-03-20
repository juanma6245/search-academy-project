package co.empathy.academy.search.common.factory;

import co.empathy.academy.search.common.CSVtype;
import co.empathy.academy.search.model.title.*;

public class FactoryTitle implements IFactoryTitle{

    private static final FactoryTitle instance = new FactoryTitle();
    private FactoryTitle() {
    }

    public static FactoryTitle getInstance() {
        return instance;
    }
    @Override
    public Title getTitle(CSVtype type, String[] data) {
        switch (type){
            case AKA:
                String[] types = data[5].split(",");
                String[] attributes = data[6].split(",");
                return new Aka(
                        (String) data[0],
                        Integer.valueOf(data[1]) ,
                        (String) data[2],
                        (String) data[3],
                        (String) data[4],
                        types,
                        attributes,
                        (String) data[7]
                );
            case BASIC:
                Boolean adult;
                if (data[4].equals("0")) {
                    adult = false;
                } else {
                    adult = true;
                }
                String[] genres = data[8].split(",");
                return new Basic(
                        (String) data[0],
                        (String) data[1],
                        (String) data[2],
                        (String) data[3],
                        adult,
                        (String) data[5],
                        (String) data[6],
                        Integer.valueOf(data[7]),
                        genres
                );
            case CREW:
                String[] directors = data[1].split(",");
                String[] writers = data[2].split(",");
                return new Crew(
                        (String) data[0],
                        directors,
                        writers
                );
            case EPISODE:
                return new Episode(
                        (String) data[0],
                        (String) data[1],
                        Integer.valueOf(data[2]),
                        Integer.valueOf(data[3])
                );
            case PRINCIPAL:
                return new Principal(
                        (String) data[0],
                        (String) data[1],
                        (String) data[2],
                        (String) data[3],
                        (String) data[4],
                        (String) data[5]);
            case RATING:
                return new Rating(
                        (String) data[0],
                        Double.valueOf(data[1]),
                        Integer.valueOf(data[2])
                );
            default:
                return null;
        }
     }
}

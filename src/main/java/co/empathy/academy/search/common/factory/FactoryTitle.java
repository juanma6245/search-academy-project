package co.empathy.academy.search.common.factory;

import co.empathy.academy.search.common.TSVtype;
import co.empathy.academy.search.model.title.*;

public class FactoryTitle implements IFactoryTitle{

    private static final FactoryTitle instance = new FactoryTitle();
    private FactoryTitle() {
    }

    public static FactoryTitle getInstance() {
        return instance;
    }
    @Override
    public Title getTitle(TSVtype type, String[] data) {
        switch (type){
            case AKA:
                String[] types = data[5].split(",");
                String[] attributes = data[6].split(",");
                return new Aka(
                        data[0],
                        Integer.valueOf(data[1]) ,
                        data[2],
                        data[3],
                        data[4],
                        types,
                        attributes,
                        data[7]
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
                        data[0],
                        data[1],
                        data[2],
                        data[3],
                        adult,
                        data[5],
                        data[6],
                        data[7],
                        genres
                );
            case CREW:
                String[] directors = data[1].split(",");
                String[] writers = data[2].split(",");
                return new Crew(
                        data[0],
                        directors,
                        writers
                );
            case EPISODE:
                return new Episode(
                        data[0],
                        data[1],
                        Integer.valueOf(data[2]),
                        Integer.valueOf(data[3])
                );
            case PRINCIPAL:
                return new Principal(
                        data[0],
                        data[1],
                        data[2],
                        data[3],
                        data[4],
                        data[5]);
            case RATING:
                return new Rating(
                        data[0],
                        Double.valueOf(data[1]),
                        Integer.valueOf(data[2])
                );
            default:
                return null;
        }
     }
}

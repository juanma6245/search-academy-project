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
    public Title getTitle(CSVtype type, Object[] data) {
        switch (type){
            case AKA:
                return new Aka(
                        (String) data[0],
                        (Integer) data[1],
                        (String) data[2],
                        (String) data[3],
                        (String) data[4],
                        (String[]) data[5],
                        (String[]) data[6],
                        (String) data[7]
                );
            case BASIC:
                return new Basic(
                        (String) data[0],
                        (String) data[1],
                        (String) data[2],
                        (String) data[3],
                        (Boolean) data[4],
                        (String) data[5],
                        (String) data[6],
                        (Integer) data[7],
                        (String[]) data[8]
                );
            case CREW:
                return new Crew(
                        (String) data[0],
                        (String[]) data[1],
                        (String[]) data[2]);
            case EPISODE:
                return new Episode(
                        (String) data[0],
                        (String) data[1],
                        (Integer) data[2],
                        (Integer) data[3]);
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
                        (Double) data[1],
                        (Integer) data[2]);
            default:
                return null;
        }
     }
}

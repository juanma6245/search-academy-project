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
                boolean isOriginalTitle;
                if (data[7].equals("0")) {
                    isOriginalTitle = false;
                } else {
                    isOriginalTitle = true;
                }
                return new Aka(
                        data[0],
                        Integer.valueOf(data[1]) ,
                        data[2],
                        data[3],
                        data[4],
                        types,
                        attributes,
                        isOriginalTitle
                );
            case BASIC:
                Boolean adult;
                if (data[4].equals("0")) {
                    adult = false;
                } else {
                    adult = true;
                }
                int startYear;
                if (data[5].equals("\\N")) {
                    startYear = 0;
                } else {
                    startYear = Integer.valueOf(data[5]);
                }
                int endYear;
                if (data[6].equals("\\N")) {
                    endYear = 0;
                } else {
                    endYear = Integer.valueOf(data[6]);
                }
                String[] genres = data[8].split(",");
                int runtimeMinutes;
                if (data[7].equals("\\N")) {
                    runtimeMinutes = 0;
                } else {
                    runtimeMinutes = Integer.valueOf(data[7]);
                }
                return new Basic(
                        data[0],
                        data[1],
                        data[2],
                        data[3],
                        adult,
                        startYear,
                        endYear,
                        runtimeMinutes,
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
                int seasonNumber;
                if (data[2].equals("\\N")) {
                    seasonNumber = 0;
                } else {
                    seasonNumber = Integer.valueOf(data[2]);
                }
                int episodeNumber;
                if (data[3].equals("\\N")) {
                    episodeNumber = 0;
                } else {
                    episodeNumber = Integer.valueOf(data[3]);
                }
                return new Episode(
                        data[0],
                        data[1],
                        seasonNumber,
                        episodeNumber
                );
            case PRINCIPAL:
                return new Principal(
                        data[0],
                        Integer.valueOf(data[1]),
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

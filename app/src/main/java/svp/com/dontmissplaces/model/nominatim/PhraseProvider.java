package svp.com.dontmissplaces.model.nominatim;

import java.util.Objects;

import svp.com.dontmissplaces.db.Place;

//http://wiki.openstreetmap.org/wiki/Nominatim/Special_Phrases/EN
public class PhraseProvider {
    public enum PhraseType {
        Undefined(0),
        Food(1),
        Monument(2),
        Station(3);


        private final int code;

        PhraseType(int code) {
            this.code = code;
        }
        public int toInt() {
            return code;
        }

        public static PhraseType get(int resultCode) {
            return PhraseType.values()[resultCode];
        }
    }

    public static PhraseType getType(String type){
        for (String item : food){
            if(Objects.equals(item, type)){
                return PhraseType.Food;
            }
        }
        return PhraseType.Undefined;
    }

    public String[] getPhrases(int zoomLevel) {
        //3 -> 19

        if(zoomLevel > 12 && zoomLevel < 16) {
            //metro station
        }

        if(zoomLevel == 16){
            //bus station
            return monuments;
        }
        if(zoomLevel > 16){
            return food;
        }
        return new String[0];
    }

    private static final String[] food =new String[]{"bar", "pub", "cafe", "fast_food", "restaurant"};
    private static final String[] monuments =new String[]{};

}

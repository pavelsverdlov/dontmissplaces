package svp.com.dontmissplaces.model.nominatim;

//http://wiki.openstreetmap.org/wiki/Nominatim/Special_Phrases/EN
public class PhraseProvider {

    public String[] getPhrases(int zoomLevel) {
        //3 -> 19
        if(zoomLevel == 16){
            return monuments;
        }
        if(zoomLevel > 16){
            return food;
        }
        return new String[0];
    }

    private final String[] food =new String[]{"bar", "pub", "cafe", "fast_food", "restaurant"};
    private final String[] monuments =new String[]{};

}

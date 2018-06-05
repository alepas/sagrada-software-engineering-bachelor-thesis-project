package it.polimi.ingsw.view.gui;

public class WpcConstants {
    private static final String WPC1= "Virtus";
    private static final String WPC2= "Kaleidoscopic Dreams";
    private static final String WPC3= "Vialux";
    private static final String WPC4= "Aurorae Magnificus";
    private static final String WPC5= "Bellesguard";
    private static final String WPC6= "Sun Catcher";
    private static final String WPC7= "Symphony Of Life";
    private static final String WPC8= "Firmitas";
    private static final String WPC9= "Industria";
    private static final String WPC10= "Aurorae Sagradis";
    private static final String WPC11= "Batllò";
    private static final String WPC12= "Shadow Thief";
    private static final String WPC13= "Fractal Drops";
    private static final String WPC14= "Gravitas";
    private static final String WPC15= "Chromatic Splendor";
    private static final String WPC16= "Lux Astram";
    private static final String WPC17= "Lux Celestial";
    private static final String WPC18= "Firelight";
    private static final String WPC19= "Ripples Of Light";
    private static final String WPC20= "Water Of Life";
    private static final String WPC21= "Comitas";
    private static final String WPC22= "Lux Mundi";
    private static final String WPC23= "Fulgor Del Cielo";
    private static final String WPC24= "Sun's Glory";

    public static String setWpcName(String id){
        switch (id){
            case "1":
                return WPC1;
            case "2":
                return WPC2;
            case "3":
                return WPC3;
            case "4":
                return WPC4;
            case "5":
                return WPC5;
            case "6":
                return WPC6;
            case "7":
                return WPC7;
            case "8":
                return WPC8;
            case "9":
                return WPC9;
            case "10":
                return WPC10;
            case "11":
                return WPC11;
            case "12":
                return WPC12;
            case "13":
                return WPC13;
            case "14":
                return WPC14;
            case "15":
                return WPC15;
            case "16":
                return WPC16;
            case "17":
                return WPC17;
            case "18":
                return WPC18;
            case "19":
                return WPC19;
            case "20":
                return WPC20;
            case "21":
                return WPC21;
            case "22":
                return WPC22;
            case "23":
                return WPC23;
            case "24":
                return WPC24;
            default:
                break;
        }
        return null;
    }
}

package betaar.pk;

import android.util.Log;

import java.util.ArrayList;

import betaar.pk.Models.DataIds;
import betaar.pk.Models.MyProductsGetterSetter;

public class Arrays {

    public static ArrayList<DataIds> product_types = new ArrayList<DataIds>(){{
        add(new DataIds("Select Product Type","0"));
        add(new DataIds("Dairy Solution","1"));
        add(new DataIds("Pet Solution","2"));
        add(new DataIds("Equine Solution","3"));
        add(new DataIds("Birds Solution","4"));
        add(new DataIds("Wild Life Solution","5"));
    }};
    
    public static ArrayList<DataIds> product_categories_for_dairy_organization = new ArrayList<DataIds>(){{
        add(new DataIds("Select Category","0"));
        add(new DataIds("Accessories","1"));
        add(new DataIds("Feed","2"));
        add(new DataIds("Animals(sale & purc)","3"));
        add(new DataIds("Milking Machine","4"));
        add(new DataIds("MedicineProducts","5"));
        add(new DataIds("Shed Constructions","6"));
        add(new DataIds("Labs","7"));
        add(new DataIds("Labour","8"));
        add(new DataIds("Land On Rent","9"));
        add(new DataIds("Sprays","10"));
        add(new DataIds("Milk(sale & purc)","11"));
        add(new DataIds("Dairy Crops Seed","12"));
    }};

    public static ArrayList<DataIds> product_categories_for_pets_organization = new ArrayList<DataIds>(){{
        add(new DataIds("Select Category","0"));
        add(new DataIds("Accessories","13"));
        add(new DataIds("Feed","14"));
        add(new DataIds("Pets (sale & pur)","15"));
        add(new DataIds("Vaccinations","16"));
        add(new DataIds("Medicine Products","17"));
        add(new DataIds("Labour","18"));
        add(new DataIds("Cages/Housing Const","19"));
        add(new DataIds("Labs","20"));
        add(new DataIds("Pet Trainer","21"));
        add(new DataIds("Breeder Services","22"));
    }};

    public static ArrayList<DataIds> product_categories_for_equine_organization = new ArrayList<DataIds>(){{
        add(new DataIds("Select Category","0"));
        add(new DataIds("Accessories","23"));
        add(new DataIds("Feed","24"));
        add(new DataIds("Equine (sale & pur)","25"));
        add(new DataIds("Vaccinations","26"));
        add(new DataIds("Medicine Products","27"));
        add(new DataIds("Shed Construction","28"));
        add(new DataIds("Labs","29"));
        add(new DataIds("Labour","30"));
        add(new DataIds("Horse Trainer","31"));
        add(new DataIds("Breeder Services","32"));
    }};

    public static ArrayList<DataIds> product_categories_for_bird_organization = new ArrayList<DataIds>(){{
        add(new DataIds("Select Category","0"));
        add(new DataIds("Accessories","33"));
        add(new DataIds("Feed","34"));
        add(new DataIds("Birds (sale & pur)","35"));
        add(new DataIds("Vaccinations","36"));
        add(new DataIds("Medicine Products","37"));
        add(new DataIds("Cages/Shed","38"));
        add(new DataIds("Labs","39"));
        add(new DataIds("Labour","40"));
        add(new DataIds("Egg (sale & pur)","41"));
        add(new DataIds("Shed for Rent","42"));
    }};

    public static ArrayList<DataIds> product_categories_for_wildLife_organization = new ArrayList<DataIds>(){{
        add(new DataIds("Select Category","0"));
        add(new DataIds("Accessories","43"));
        add(new DataIds("Feed","44"));
        add(new DataIds("Animals (sale & pur)","45"));
        add(new DataIds("Vaccinations","46"));
        add(new DataIds("Medicine Products","47"));
        add(new DataIds("Aquariums/Cages","48"));
        add(new DataIds("Labs","49"));
        add(new DataIds("Labour","50"));
    }};

    public static ArrayList<DataIds> product_sub_categories_for_dairy_accesories = new ArrayList<DataIds>(){{
        add(new DataIds("Select Category","0"));
        add(new DataIds("Surgical Instruments","1"));
        add(new DataIds("Fodder Cutting Machine","2"));
        add(new DataIds("Fans/Shower","3"));
        add(new DataIds("Tags/Tag Markers","4"));
        add(new DataIds("Calf Cages/Mats","5"));
        add(new DataIds("TMR/Tractor/Trolley","6"));
        add(new DataIds("Bio-Gas Plant","7"));
        add(new DataIds("Cattle Crush","8"));
        add(new DataIds("Semen and Container","80"));
        add(new DataIds("Other","9"));
    }};

    public static ArrayList<DataIds> product_sub_categories_for_dairy_feed = new ArrayList<DataIds>(){{
        add(new DataIds("Select Category","0"));
        add(new DataIds("Silage","10"));
        add(new DataIds("Fresh Fooder","11"));
        add(new DataIds("Wanda","12"));
        add(new DataIds("Wanda Ingredient","13"));
        add(new DataIds("Hay/Beet Plup","14"));
        add(new DataIds("Wheat Straw","15"));
        add(new DataIds("Feed Additives","16"));
        add(new DataIds("Other","17"));
    }};

    public static ArrayList<DataIds> product_sub_categories_for_dairy_animals = new ArrayList<DataIds>(){{
        add(new DataIds("Select Category","0"));
        add(new DataIds("Local Dairy Cows","18"));
        add(new DataIds("Imported Dairy Cows","19"));
        add(new DataIds("Buffalo","20"));
        add(new DataIds("Sheep","21"));
        add(new DataIds("Goat","22"));
        add(new DataIds("Bulls","23"));
        add(new DataIds("Qurban Animals For Eid","24"));
        add(new DataIds("Other","25"));
    }};

    public static ArrayList<DataIds> product_sub_categories_for_dairy_milking_parlour = new ArrayList<DataIds>(){{
        add(new DataIds("Select Category","0"));
        add(new DataIds("Mobile Machines","26"));
        add(new DataIds("Milking rows","27"));
        add(new DataIds("Milking Parlour","28"));
        add(new DataIds("Spare Parts","29"));
        add(new DataIds("Acid/Alkali For CIP","30"));
        add(new DataIds("Teat Dip Solutions","31"));
        add(new DataIds("Chillers","32"));
        add(new DataIds("Other","33"));
    }};

    public static ArrayList<DataIds> product_sub_categories_for_dairy_medicine_products = new ArrayList<DataIds>(){{
        add(new DataIds("Select Category","0"));
        add(new DataIds("Vaccinations","34"));
        add(new DataIds("Dewormer","35"));
        add(new DataIds("Antibiotics","36"));
        add(new DataIds("Hormones","37"));
        add(new DataIds("NSAIDS/Steroids","38"));
        add(new DataIds("Supportive","39"));
        add(new DataIds("Other","40"));
    }};

    public static ArrayList<DataIds> product_sub_categories_for_dairy_sheed_construction = new ArrayList<DataIds>(){{
        add(new DataIds("Select Category","0"));
        add(new DataIds("Open Shed","41"));
        add(new DataIds("Semi Control Shed","42"));
        add(new DataIds("Control Shed","43"));
        add(new DataIds("Desi Structure","44"));
        add(new DataIds("Shed Material","45"));
        add(new DataIds("Other","46"));
    }};

    public static ArrayList<DataIds> product_sub_categories_for_dairy_labs = new ArrayList<DataIds>(){{
        add(new DataIds("Select Category","0"));
        add(new DataIds("Feed Analysis","47"));
        add(new DataIds("Blood Analysis","48"));
        add(new DataIds("Fecal Analysis","49"));
        add(new DataIds("X-Rays","50"));
        add(new DataIds("Ultrasounds","51"));
        add(new DataIds("Water Analysis","52"));
    }};

    public static ArrayList<DataIds> product_sub_categories_for_dairy_labour = new ArrayList<DataIds>(){{
        add(new DataIds("Select Category","0"));
        add(new DataIds("Milker","53"));
        add(new DataIds("Feeding Boy","54"));
        add(new DataIds("Supervisor","55"));
        add(new DataIds("Thekedar","56"));
        add(new DataIds("Cook","57"));
        add(new DataIds("Security Guard","58"));
        add(new DataIds("Electrician/Welder","59"));
        add(new DataIds("Other","60"));
    }};

    public static ArrayList<DataIds> product_sub_categories_for_dairy_land_on_rent = new ArrayList<DataIds>(){{
        add(new DataIds("Select Category","0"));
        add(new DataIds("Shed For Rent","61"));
        add(new DataIds("Agriculture Land For Rent","62"));
    }};

    public static ArrayList<DataIds> product_sub_categories_for_dairy_sprays = new ArrayList<DataIds>(){{
        add(new DataIds("Select Category","0"));
        add(new DataIds("Pesticides","63"));
        add(new DataIds("Insecticides","64"));
        add(new DataIds("Disinfectants","65"));
        add(new DataIds("Bactericidal","66"));
        add(new DataIds("Other","67"));
    }};

    public static ArrayList<DataIds> product_sub_categories_for_dairy_milk_sale_pur = new ArrayList<DataIds>(){{
        add(new DataIds("Select Category","0"));
        add(new DataIds("Cow Milk","68"));
        add(new DataIds("Buffalo Milk","69"));
        add(new DataIds("Goat Milk","70"));
        add(new DataIds("Camel Milk","71"));
        add(new DataIds("Cow & Buffalo Milk","72"));
        add(new DataIds("Other","73"));
    }};

    public static ArrayList<DataIds> product_sub_categories_for_dairy_crops_seed = new ArrayList<DataIds>(){{
        add(new DataIds("Select Category","0"));
        add(new DataIds("Corn","74"));
        add(new DataIds("Alfalfa","75"));
        add(new DataIds("Sorghum","76"));
        add(new DataIds("Oats","77"));
        add(new DataIds("Rhodes Grass","78"));
        add(new DataIds("Other","79"));
    }};

    public static ArrayList<DataIds> job_categories = new ArrayList<DataIds>(){{
        add(new DataIds("Select Job","0"));
        add(new DataIds("Dairy Farm job","1"));
        add(new DataIds("Dairy Marketing job","2"));
        add(new DataIds("Poultry Farm job","3"));
        add(new DataIds("Poultry Marketing job","4"));
        add(new DataIds("Zoo job","5"));
        add(new DataIds("Equine farm job","6"));
        add(new DataIds("Pet hospital job","7"));
        add(new DataIds("Other","8"));
    }};

    public static ArrayList<DataIds> salary_range = new ArrayList<DataIds>(){{
        add(new DataIds("Select Salary Range","0"));
        add(new DataIds("5000-15000","1"));
        add(new DataIds("1500-25000","2"));
        add(new DataIds("2500-40000","3"));
        add(new DataIds("65000-80000","4"));
        add(new DataIds("80000-100000","5"));
        add(new DataIds("100000+","6"));
    }};

    public static ArrayList<DataIds> price_units = new ArrayList<DataIds>(){{
        add(new DataIds("Unit","0"));
        add(new DataIds("PKR","1"));
        add(new DataIds("$","2"));
        add(new DataIds("€","3"));
        add(new DataIds("£","4"));
    }};

    public static ArrayList<DataIds> specialities = new ArrayList<DataIds>(){{
        add(new DataIds("Select Specialities","0"));
        add(new DataIds("Treatment","1"));
        add(new DataIds("Nutritionist","2"));
        add(new DataIds("Breeding","3"));
        add(new DataIds("Surgeon","4"));
    }};

    public static ArrayList<DataIds> dairy_categories = new ArrayList<DataIds>(){{
        add(new DataIds("Select Dairy Category","0"));
        add(new DataIds("Cow","1"));
        add(new DataIds("Buffalo","2"));
        add(new DataIds("Sheep","3"));
        add(new DataIds("Goat","4"));
        add(new DataIds("Camel","13"));
    }};

    public static ArrayList<DataIds> pets_categories = new ArrayList<DataIds>(){{
        add(new DataIds("Select Pet Category","0"));
        add(new DataIds("Dog","6"));
        add(new DataIds("Cat","5"));
        add(new DataIds("Rabbit","18"));
    }};

    public static ArrayList<DataIds> equine_categories = new ArrayList<DataIds>(){{
        add(new DataIds("Select Equine Category","0"));
        add(new DataIds("Donkey","9"));
        add(new DataIds("Mule","17"));
        add(new DataIds("Horse","8"));
    }};

    public static ArrayList<DataIds> birds_categories = new ArrayList<DataIds>(){{
        add(new DataIds("Select Birds Category","0"));
        add(new DataIds("Ostrich","12"));
        add(new DataIds("Poultry Birds","14"));
        add(new DataIds("Fancy Birds","15"));
        add(new DataIds("Game Birds","16"));
    }};

    public static ArrayList<DataIds> other_categories = new ArrayList<DataIds>(){{
        add(new DataIds("Select Category","0"));
        add(new DataIds("Fish","19"));
        add(new DataIds("Lion","20"));
        add(new DataIds("Deer","21"));
        add(new DataIds("Monkey","22"));
        add(new DataIds("Others","23"));
    }};

    public static ArrayList<DataIds> only_dairy = new ArrayList<DataIds>(){{
        add(new DataIds("Select Category","0"));
        add(new DataIds("Dairy Solution","1"));
    }};

    public static ArrayList<DataIds> only_pet = new ArrayList<DataIds>(){{
        add(new DataIds("Select Category","0"));
        add(new DataIds("Pets Solution","2"));
    }};

    public static ArrayList<DataIds> only_equine = new ArrayList<DataIds>(){{
        add(new DataIds("Select Category","0"));
        add(new DataIds("Equine Solution","3"));
    }};

    public static ArrayList<DataIds> only_bird = new ArrayList<DataIds>(){{
        add(new DataIds("Select Category","0"));
        add(new DataIds("Bird Solution","4"));
    }};

    public static ArrayList<DataIds> only_wild_life = new ArrayList<DataIds>(){{
        add(new DataIds("Select Category","0"));
        add(new DataIds("Wild Life Solution","5"));
    }};

    public static ArrayList<DataIds> product_categories_for_dairy = new ArrayList<DataIds>(){{
        add(new DataIds("Select Category","0"));
        add(new DataIds("Animals (sale & pur)","3"));
        add(new DataIds("Labour","8"));
        add(new DataIds("Land on Rent","9"));
        add(new DataIds("Milk (sale & purc)","11"));

    }};

    public static ArrayList<DataIds> product_categories_for_pets = new ArrayList<DataIds>(){{
        add(new DataIds("Select Category","0"));
        add(new DataIds("Pets (sale & pur)","15"));
        add(new DataIds("Labour","18"));
        add(new DataIds("Pet Trainer","21"));
        add(new DataIds("Breeder Services","22"));
    }};

    public static ArrayList<DataIds> product_categories_for_equine = new ArrayList<DataIds>(){{
        add(new DataIds("Select Category","0"));
        add(new DataIds("Equine (sale & pur)","25"));
        add(new DataIds("Labour","30"));
        add(new DataIds("Horse Trainer","31"));
        add(new DataIds("Breeder Services","32"));

    }};

    public static ArrayList<DataIds> product_categories_for_bird = new ArrayList<DataIds>(){{
        add(new DataIds("Select Category","0"));
        add(new DataIds("Birds (sale & pur)","35"));
        add(new DataIds("Cages/Shed","38"));
        add(new DataIds("Labour","40"));
        add(new DataIds("Egg (sale & pur)","41"));
        add(new DataIds("Shed for Rent","42"));

    }};

    public static ArrayList<DataIds> product_categories_for_other = new ArrayList<DataIds>(){{
        add(new DataIds("Select Category","0"));
        add(new DataIds("Animals (sale & pur)","43"));
        add(new DataIds("Aquariums/Cages","48"));
        add(new DataIds("Labour","50"));
    }};

    public static ArrayList<DataIds> quantity = new ArrayList<DataIds>(){{
        add(new DataIds("Quantity","0"));
        add(new DataIds("1","1"));
        add(new DataIds("2","2"));
        add(new DataIds("3","3"));
        add(new DataIds("4","4"));
        add(new DataIds("5","5"));
        add(new DataIds("6","6"));
        add(new DataIds("7","7"));
        add(new DataIds("8","8"));
        add(new DataIds("9","9"));
        add(new DataIds("10","10"));
    }};

    public static ArrayList<DataIds> qualification_type = new ArrayList<DataIds>(){{
        add(new DataIds("Select","0"));
        add(new DataIds("LAD","1"));
        add(new DataIds("DVM","2"));
    }};

    public static ArrayList<DataIds> languages = new ArrayList<DataIds>(){{
        add(new DataIds("Select Language","0"));
        add(new DataIds("English","1"));
        add(new DataIds("Urdu","2"));
    }};

    public static ArrayList<DataIds> Surgeon = new ArrayList<DataIds>(){{
        add(new DataIds("Select","0"));
        add(new DataIds("Surgery1","1"));
        add(new DataIds("Surgery2","2"));
        add(new DataIds("Surgery3","3"));
        add(new DataIds("Surgery4","4"));
        add(new DataIds("Surgery5","5"));
    }};

    public static String getID(ArrayList<DataIds> array, String find){

        String category_id = "0";

        for (int i = 0 ; i < array.size() ; i++) {

            if (array.get(i).getName().toLowerCase().equals(find.toLowerCase()) || array.get(i).getName().equals(find)){

                Log.e("TEST","FIND: " + find.toLowerCase());
                category_id = array.get(i).getId();

                break;

            }

        }

        //Log.e("TAG","ID: " + category_id);

        return category_id;

    }

    public static String getName(ArrayList<DataIds> array, String find){

        String category_id = "0";

        for (int i = 0 ; i < array.size() ; i++) {

            if (array.get(i).getId().equals(find)){

                category_id = array.get(i).getName();

                break;

            }

        }

        //Log.e("TAG","ID: " + category_id);

        return category_id;

    }

}

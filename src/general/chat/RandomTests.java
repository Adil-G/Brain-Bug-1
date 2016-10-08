package general.chat;

/**
 * Created by corpi on 2016-10-07.
 */
public class RandomTests {
    public static void main(String[] args)
    {
        String q = "journal of business venturing 17 (2002) 519\\u2013535\\r\\nmetaphors and meaning\\r\\na demo cultural model of us entrepreneurship\\r\\nsarah drakopoulou dodd*\\r\\nfaculty of business and management, the american college of greece, 6 gravias street, aghia paraskevi,\\r\\nathens, greece\\r\\nthe centre for entrepreneurship, department of management studies, university of aberdeen,\\r\\nedward wright building, dunbar street, old aberdeen, aberdeen ab24 3qy, scotland, uk\\r\\nabstract\\r\\na grounded cultural model of demo entrepreneurship is developed by analysing the metaphors that\\r\\nentrepreneurs use to give meaning to entrepreneurship in their life-and-business narratives -";
        System.out.println(q.toLowerCase().matches(".*?\\b"+"life"+".*?"));
    }
}

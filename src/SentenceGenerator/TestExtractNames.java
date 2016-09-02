package SentenceGenerator;
import com.knowledgebooks.nlp.ExtractNames;
import com.knowledgebooks.nlp.KeyPhraseExtractionAndSummary;
import com.knowledgebooks.nlp.util.ScoredList;

/**
 * Created by IntelliJ IDEA.
 * User: markw
 * Date: Feb 7, 2010
 * Time: 3:11:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestExtractNames {
    /**
     * @param args
     */
    static public void main(String[] args) {
        ExtractNames extractNames = new ExtractNames();
        // initialize everything, before printing any output - trying to see what is taking so long!
        if (args.length > 0) {
            ScoredList[] ret = extractNames.getProperNames(args[0]);
            System.out.println("Human names: " + ret[0].getValuesAsString());
            System.out.println("Place names: " + ret[1].getValuesAsString());
        } else {
            extractNames.isPlaceName("Paris");
            extractNames.isHumanName("President Bush");
            extractNames.isHumanName("President George Bush");
            extractNames.isHumanName("President George W. Bush");
            System.out.println("Initialization complete....");
            System.out.println("Paris: " + extractNames.isPlaceName("Paris"));
            System.out.println("Mexico: " + extractNames.isPlaceName("Mexico"));
            System.out.println("Fresno: " + extractNames.isPlaceName("Fresno"));
            System.out.println("Moscow: " + extractNames.isPlaceName("Moscow"));
            System.out.println("France: " + extractNames.isPlaceName("France"));
            System.out.println("Los Angeles: " + extractNames.isPlaceName("Los Angeles"));
            System.out.println("President Bush: " + extractNames.isHumanName("President Bush"));
            System.out.println("President George Bush: " + extractNames.isHumanName("President George Bush"));
            System.out.println("President George W. Bush: " + extractNames.isHumanName("President George W. Bush"));
            System.out.println("George W. Bush: " + extractNames.isHumanName("George W. Bush"));
            System.out.println("Senator Barbara Boxer: " + extractNames.isHumanName("Senator Barbara Boxer"));
            System.out.println("King Smith: " + extractNames.isHumanName("King Smith"));
            ScoredList[] ret = extractNames.getProperNames("George Bush played golf. President George W. Bush went to London England, Paris France and Mexico to see Mary Smith in Moscow. President Bush will return home Monday.");
            System.out.println("ret = " + ret);
            System.out.println("Human names: " + ret[0].getValuesAsString());
            System.out.println("Place names: " + ret[1].getValuesAsString());
            System.out.println("\n\n\n");

            // for book example:
            ExtractNames names = new ExtractNames();
            System.out.println("Los Angeles: " +
                    names.isPlaceName("Los Angeles"));
            System.out.println("President Bush: " +
                    names.isHumanName("President Bush"));
            System.out.println("President George Bush: " +
                    names.isHumanName("President George Bush"));
            System.out.println("President George W. Bush: " +
                    names.isHumanName("President George W. Bush"));
            ScoredList[] ret1 = names.getProperNames(
                    "George Bush played golf. President  George W. Bush went to London England, Paris France and Mexico to see Mary  Smith in Moscow. President Bush will return home Monday.");
            System.out.println("Human names: " +
                    ret1[0].getValuesAsString());
            System.out.println("Place names: " +
                    ret1[1].getValuesAsString());
            System.out.println("taylor swift" +
                    names.isHumanName("Taylor Swift"));

            // also text summarization:
            KeyPhraseExtractionAndSummary kp = new KeyPhraseExtractionAndSummary("" +
                    "Elephants are large mammals of the family Elephantidae and the order Proboscidea. Two species are traditionally recognised, the African elephant (Loxodonta africana) and the Asian elephant (Elephas maximus), although some evidence suggests that African bush elephants and African forest elephants are separate species (L. africana and L. cyclotis respectively). Elephants are scattered throughout sub-Saharan Africa, South Asia, and Southeast Asia. Elephantidae is the only surviving family of the order Proboscidea; other, now extinct, members of the order include deinotheres, gomphotheres, mammoths, and mastodons. Male African elephants are the largest extant terrestrial animals and can reach a height of 4 m (13 ft) and weigh 7,000 kg (15,000 lb). All elephants have several distinctive features, the most notable of which is a long trunk or proboscis, used for many purposes, particularly breathing, lifting water and grasping objects. Their incisors grow into tusks, which can serve as weapons and as tools for moving objects and digging. Elephants' large ear flaps help to control their body temperature. Their pillar-like legs can carry their great weight. African elephants have larger ears and concave backs while Asian elephants have smaller ears and convex or level backs.\n" +
                    "\n" +
                    "Elephants are herbivorous and can be found in different habitats including savannahs, forests, deserts and marshes. They prefer to stay near water. They are considered to be keystone species due to their impact on their environments. Other animals tend to keep their distance where predators such as lions, tigers, hyenas, and wild dogs usually target only the young elephants (or \"calves\"). Females (\"cows\") tend to live in family groups, which can consist of one female with her calves or several related females with offspring. The groups are led by an individual known as the matriarch, often the oldest cow. Elephants have a fission–fusion society in which multiple family groups come together to socialise. Males (\"bulls\") leave their family groups when they reach puberty, and may live alone or with other males. Adult bulls mostly interact with family groups when looking for a mate and enter a state of increased testosterone and aggression known as musth, which helps them gain dominance and reproductive success. Calves are the centre of attention in their family groups and rely on their mothers for as long as three years. Elephants can live up to 70 years in the wild. They communicate by touch, sight, smell and sound; elephants use infrasound, and seismic communication over long distances. Elephant intelligence has been compared with that of primates and cetaceans. They appear to have self-awareness and show empathy for dying or dead individuals of their kind.\n" +
                    "\n" +
                    "African elephants are listed as vulnerable by the International Union for Conservation of Nature (IUCN), while the Asian elephant is classed as endangered. One of the biggest threats to elephant populations is the ivory trade, as the animals are poached for their ivory tusks. Other threats to wild elephants include habitat destruction and conflicts with local people. Elephants are used as working animals in Asia. In the past they were used in war; today, they are often controversially put on display in zoos, or exploited for entertainment in circuses. Elephants are highly recognisable and have been featured in art, folklore, religion, literature and popular culture.");
            System.out.println("\n\nTesting summary:\n" + kp.getSummary());
        }
    }
}
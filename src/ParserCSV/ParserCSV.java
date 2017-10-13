package ParserCSV;

import com.sun.deploy.util.OrderedHashSet;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

public class ParserCSV {

    private static final List<Set<String>> conferencesMembers = new ArrayList<>();
    private static final Set<String> wokMembers = new HashSet<>();
    private static final Set<String> countries = new HashSet<>();

    //initializes static list of Set<String>
    static {
        conferencesMembers.add(new LinkedHashSet<>());
        conferencesMembers.add(new LinkedHashSet<>());
        conferencesMembers.add(new LinkedHashSet<>());
        conferencesMembers.add(new LinkedHashSet<>());
        conferencesMembers.add(new LinkedHashSet<>());
        conferencesMembers.add(new LinkedHashSet<>());
    }

    public static void parseFile(String csvFilePath){
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(csvFilePath))) {

            String line;

            //skip first line
            bufferedReader.readLine();

                while ((line = bufferedReader.readLine()) != null) {
                    parseLine(line);
                }
        }
        catch (Exception e){
            e.getMessage();
        }
    }

    private static void parseLine(String line){

        String [] members = line.split(";");

        for (int i = 0; i < members.length; i++) {

            String memberEmailAdress = members[i];

            // check if not whitespaces
            if (memberEmailAdress.trim().length() > 0){
                //add person to the right conference
                conferencesMembers.get(i).add(memberEmailAdress);

                //store unique wok members in set
                if(members[i].contains("wok")){
                    wokMembers.add(memberEmailAdress);
                }

                //store unique countries in set
                String s = new String (memberEmailAdress);
                String[] emailAdressParts = s.split("\\.");
                countries.add(emailAdressParts[1]);
            }
        }
    }

    private static void printConferenceParticipants(int conferenceNumber){
        conferencesMembers.get(conferenceNumber).forEach( System.out::println);
//        conferencesMembers.get(conferenceNumber).forEach(name -> System.out.println(name));
    }

    public static void main(String[] args) {
        parseFile("conferences_data.csv");
//        parseFile("test.csv");

        //------------ number of participants of more than one conference------------//
        Set<String> set = new HashSet<>();
        Set<String> moreThanOneConferenceParticipants = new HashSet<>();
        List<String> all = new ArrayList<>();

        for (int i = 0; i < conferencesMembers.size(); i++) {
            all.addAll(conferencesMembers.get(i));
        }

        for (String s : all){
            if (set.contains(s)){
                moreThanOneConferenceParticipants.add(s);
            }
            set.add(s);
        }

        System.out.println(moreThanOneConferenceParticipants.size());

        //------------ number of WOK participants------------//
        int numberOfWokMembers  = wokMembers.size();
        System.out.println(numberOfWokMembers);

        //------------ number of countries------------//
        int numberOfCountries = countries.size();
        System.out.println(numberOfCountries);
    }
}
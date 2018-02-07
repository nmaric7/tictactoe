import java.util.*;
import java.util.stream.Collectors;

public class DrawSimulator {

//    AFC
//
//    AUSTRALIA - The Socceroos are making their fifth appearance at a World Cup
//    IRAN - Team Melli were the first Asian team to qualify for Russia 2018
//    JAPAN - The Japanese have made the Round of 16 on two occasions
//    KOREA REPUBLIC - The Taeguk Warriors have been a World Cup ever-present since 1986
//    SAUDI ARABIA - The Saudis made their World Cup debut at USA 1994
//
//    CAF
//
//    EGYPT - The Pharaohs are making their first world finals appearance since Italy 1990
//    MOROCCO - The Atlas Lions topped African zone Group C in qualification
//    NIGERIA - The Super Eagles have only missed one tournament since their debut at USA 1994
//    SENEGAL - In their debut at Korea/Japan 2002, the Lions of Teranga reached the quarter-finals
//    TUNISIA - At Argentina 1978, the Carthage Eagles became the first African team to win a World Cup match
//
//    CONCACAF
//
//    COSTA RICA - Los Ticos impressed at Brazil 2014, making the quarter-finals
//    MEXICO - The Mexicans lost just once in qualifying for Russia 2018
//    PANAMA - The central Americans are making their World Cup debut in Russia
//
//    CONMEBOL
//
//    ARGENTINA - The two-time champions appeared at the inaugural edition at Uruguay 1930
//    BRAZIL - A Seleção were the first team to qualify after hosts Russia
//    COLOMBIA - Los Cafeteros coach Jose Pekerman was in charge of his native Argentina at Germany 2006
//    PERU - The Peruvians return to the world finals for the first time in 36 years
//    URUGUAY - La Celeste were world champions as hosts in 1930 and at Brazil 1950
//
//    UEFA
//
//    BELGIUM - The Belgians went undefeated in topping UEFA Group H in qualifying
//    CROATIA - The Croatians finished third in their World Cup debut at France 1998
//    DENMARK - The Danes secured qualification with a comfortable play-off win over Republic of Ireland
//    ENGLAND - The Three Lions have made the finals 14 times from 16 qualifying campaigns
//    FRANCE - Les Bleus are making a sixth-straight world finals appearance
//    GERMANY - The defending world champions registered a 100 per cent record in qualifying
//    ICELAND - The smallest country by population ever to qualify for a World Cup
//    POLAND - The Poles finished third at Germany 1974 and Spain 1982
//    PORTUGAL - A Seleção das Quinas are making a fifth-straight finals appearance
//    RUSSIA - As the Soviet Union, the World Cup hosts finished fourth at England 1966
//    SERBIA - The Serbs topped UEFA Group D in qualification with only one defeat
//    SPAIN - Champions in 2010, Spain’s World Cup debut was at Italy 1934
//    SWEDEN - The Swedes were runners-up as World Cup hosts in 1958
//    SWITZERLAND - The Swiss have reached the quarter-finals on three occasions


//    Pot 1	Pot 2	Pot 3	Pot 4
//    Russia (hosts) (65)	 Spain (8)	 Denmark (19)	 Serbia (38)
//    Germany (1)	 Peru (10)	 Iceland (21)	 Nigeria (41)
//    Brazil (2)	  Switzerland (11)	 Costa Rica (22)	 Australia (43)
//    Portugal (3)	 England (12)	 Sweden (25)	 Japan (44)
//    Argentina (4)	 Colombia (13)	 Tunisia (28)	 Morocco (48)
//    Belgium (5)	 Mexico (16)	 Egypt (30)	 Panama (49)
//    Poland (6)	 Uruguay (17)	 Senegal (32)	 South Korea (62)
//    France (7)	 Croatia (18)	 Iran (34)	 Saudi Arabia (63)

    private static final List<Team> allTeams = Arrays.asList(
            new Team("Njemačka", "UEFA", 1),
            new Team("Brazil", "CONMEBOL", 1),
            new Team("Portugal", "UEFA", 1),
            new Team("Argentina", "CONMEBOL", 1),
            new Team("Belgija", "UEFA", 1),
            new Team("Poljska", "UEFA", 1),
            new Team("Francuska", "UEFA", 1),
            new Team("Španjolska", "UEFA", 2),
            new Team("Peru", "CONMEBOL", 2),
            new Team("Švicarska", "UEFA", 2),
            new Team("Engleska", "UEFA", 2),
            new Team("Kolumbija", "CONMEBOL", 2),
            new Team("Meksiko", "CONCACAF", 2),
            new Team("Urugvaj", "CONMEBOL", 2),
            new Team("HRVATSKA", "UEFA", 2),
            new Team("Danska", "UEFA", 3),
            new Team("Island", "UEFA", 3),
            new Team("Kostarika", "CONCACAF", 3),
            new Team("Švedska", "UEFA", 3),
            new Team("Tunis", "CAF", 3),
            new Team("Egipat", "CAF", 3),
            new Team("Senegal", "CAF", 3),
            new Team("Iran", "AFC", 3),
            new Team("Srbija", "UEFA", 4),
            new Team("Nigerija", "CAF", 4),
            new Team("Australija", "AFC", 4),
            new Team("Japan", "AFC", 4),
            new Team("Maroko", "CAF", 4),
            new Team("Panama", "CONCACAF", 4),
            new Team("Južna Koreja", "AFC", 4),
            new Team("Saudijska Arabija", "AFC", 4)
    );


    public static void main(String[] args) {
        Map<String, Integer> countryCombinations = new HashMap<>();
//        int wrongSimulations = 0;
        for (int i = 1; i <= 1_000_000; i++) {
            if (i % 1_000 == 0) {
                System.out.println("Iteration: " + i);
//                System.out.println("Wrong simulations: " + wrongSimulations);
            }
            final Team[][] draw = new Team[8][4];
            for (Team[] group : draw) {
                Arrays.fill(group, new Team("-", "-", -1));
            }
            draw[0][0] = new Team("Rusija", "UEFA", 1);
            draw(draw);

            mapCountryCombinations(countryCombinations, draw);
//            if (isEmptyPlace(draw)) wrongSimulations++;
        }
        countryCombinations.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .forEach(System.out::println); // or any other terminal method
        // System.out.println("Wrong simulations: " + wrongSimulations);
    }

    private static void mapCountryCombinations(Map<String, Integer> countryCombinations, Team[][] draw) {
        for (Team[] group : draw) {
            String key = group[0].getName() + "-" + group[1].getName() + "-" + group[2].getName() + "-" + group[3].getName();
            countryCombinations.put(key, countryCombinations.getOrDefault(key, 0) + 1);
//            for (int i = 0; i < 3; i++) {
//                for (int j = i + 1; j < 4; j++) {
//                    String key = group[i].getName() + "-" + group[j].getName();
//                    countryCombinations.put(key, countryCombinations.getOrDefault(key, 0) + 1);
//                }
//            }
        }
    }

    private static void draw(Team[][] draw) {

        drawPot(getPot(1), draw, 1);
        drawPot(getPot(2), draw, 2);
        drawPot(getPot(3), draw, 3);
        drawPot(getPot(4), draw, 4);

        // fillEmptyPlaces(draw);
        // printDraw(draw);
    }

    private static boolean isEmptyPlace(Team[][] draw) {
        for (Team[] group : draw) {
            for (Team team : group) {
                if ("-".equals(team.getName())) return true;
            }
        }
        return false;
    }

    private static boolean isPlaced(Team team, Team[][] draw) {
        for (Team[] group : draw) {
            for (Team t : group) {
                if (t.getName().equals(team.getName())) {
                    return true;
                }
            }
        }
        return false;
    }

    private static List<Team> getPot(int level) {
        final List<Team> pot = allTeams.stream()
                .filter(p -> p.getPot() == level).collect(Collectors.toList());
        Collections.sort(pot, Comparator.comparing(Team::getFederation));
        return pot;
    }


    private static void drawPot(List<Team> pot, Team[][] draw, int level) {
        if (pot == null || pot.isEmpty()) {
            return;
        }
        Collections.shuffle(pot);
        Map<Team, Integer> availableSpotsByTeam = new HashMap<>();
        for (Team team : pot) {
            availableSpotsByTeam.put(team, getAvailableSpotsByTeam(team, draw));
        }
        final List<Team> sortedList = availableSpotsByTeam.entrySet().stream()
                .sorted(Comparator.comparing(Map.Entry::getValue))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        setPlace(sortedList.get(0), sortedList, draw, level);
        sortedList.remove(0);
        drawPot(sortedList, draw, level);
    }

    private static int getAvailableSpotsByTeam(Team team, Team[][] draw) {

        int groupId = 0;
        for (Team[] group : draw) {
            if (isValidGroupForCountry(team, group)) groupId++;
        }
        return groupId;
    }

    private static void setPlace(Team team, List<Team> pot, Team[][] draw, int level) {
        List<Integer> availableGroups = findAvailableGroups(team, draw, level);
        // check is there group where only fit selected team
        for (Integer groupId : availableGroups) {
            if (isOnlyTeamFit(pot, draw[groupId])) {
                draw[groupId][team.getPot() - 1] = team;
                return;
            }
        }

        Collections.shuffle(availableGroups);
        // System.out.println(team.getName() + " >> " + availableGroups.toString() + " :: " + availableGroups.size());
        for (Integer groupId : availableGroups) {
            if (isValidGroupForCountry(team, draw[groupId])) {
                draw[groupId][team.getPot() - 1] = team;
                break;
            }
        }
    }

    private static boolean isOnlyTeamFit(List<Team> pot, Team[] group) {
        int numberOfTeamsFit = 0;
        for (Team t : pot) {
            if (isValidGroupForCountry(t, group)) numberOfTeamsFit++;
        }
        return numberOfTeamsFit == 1;
    }

    private static boolean isValidGroupForCountry(Team team, Team[] group) {
        Map<String, Integer> federations = new HashMap<>();
        for (Team t : group) {
            if (!"-".equals(t.getName())) {
                federations.put(t.getFederation(), (federations.getOrDefault(t.getFederation(), 0) + 1));
            }
        }
        Integer teamFederation = federations.getOrDefault(team.getFederation(), 0);

        // if there is only one place, and there is not UEFA team -> false
        final int occupiedPlaces = getNumberOfOccupiedPlaces(group);
        final int numberOfUEFACountries = getNumberOfUEFACountries(group);
        if (occupiedPlaces == 3 && !"UEFA".equals(team.getFederation()) && numberOfUEFACountries == 0) {
            return false;
        }

        return ("UEFA".equals(team.getFederation()) && teamFederation < 2)
            || (!"UEFA".equals(team.getFederation()) && teamFederation == 0);
    }

    private static List<Integer> findAvailableGroups(Team team, Team[][] draw, int level) {
        Map<Integer, Integer> groupAndUEFA = new HashMap<>();
        int groupId = 0;
        for (Team[] group : draw) {
            int occupiedPlaces = getNumberOfOccupiedPlaces(group);
            int numberOfUEFACountries = getNumberOfUEFACountries(group);
            if (occupiedPlaces < level && isValidGroupForCountry(team, group)) {
                // availableGroups.add(groupId);
                groupAndUEFA.put(groupId, numberOfUEFACountries);
            }
            groupId++;
        }

        return groupAndUEFA.entrySet().stream()
                // .sorted(Comparator.comparing(Map.Entry::getValue))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

//        return groupAndUEFA.entrySet()
//                .stream()
//                .sorted(Map.Entry.comparingByValue(/*Collections.reverseOrder()*/)).collect(Collectors.toMap(
//                        Map.Entry::getKey,
//                        Map.Entry::getValue,
//                        (e1, e2) -> e1,
//                        LinkedHashMap::new
//                ));
    }

    private static int getNumberOfUEFACountries(Team[] group) {
        int uefaCOuntries = 0;
        for (Team t : group) {
            if ("UEFA".equals(t.getFederation())) uefaCOuntries++;
        }
        return uefaCOuntries;
    }

    private static int getNumberOfOccupiedPlaces(Team[] group) {
        int occupiedPlaces = 0;
        for (Team t : group) {
            if (!"-".equals(t.getName())) occupiedPlaces++;
        }
        return occupiedPlaces;
    }


    private static void printDraw(Team[][] draw) {
        for (Team[] group : draw) {
            System.out.println("------------------------------------------------------------------------------------------");
            for (Team team : group) {
                System.out.print(fixedLengthString(team.getName(), 20) + " | ");
            }
            System.out.println("\n");
        }
    }

    public static String fixedLengthString(String string, int length) {
        return String.format("%1$"+length+ "s", string);
    }

}

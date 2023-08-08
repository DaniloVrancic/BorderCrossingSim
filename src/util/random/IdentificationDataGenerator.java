package util.random;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

final class IdentificationDataGenerator {

	private static Random rand = new Random();
	
	private static final int PASSPORT_NUMBER_DIGITS = 9;
	private static final int PASSPORT_NUMBER_MAX_DIGIT = 9;
	public static final String MALE = "Male";
	public static final String FEMALE = "Female";
	
	private static List<String> maleFirstNames = new ArrayList<>();
	private static List<String> femaleFirstNames = new ArrayList<>();
	private static List<String> lastNames = new ArrayList<>();
	private static List<String> nationalities = new ArrayList<>();
	private static List<String> genders = new ArrayList<>();
	
	static
	{
		new IdentificationDataGenerator(); //Call it just once to instanciate the 
	}
	
	/**
	 * Will be used to populate the lists with predetermined elements,
	 * which will be chosen at random using a pseudorandom number generator.
	 */
	private IdentificationDataGenerator()
	{
		String[] maleNamesArray = {
			    
			    "Jacob", "Ethan", "Michael", "Alexander", "William", "James", "Matthew", "David", "Joseph", "Daniel",
			    "Andrew", "Ryan", "Joshua", "Christopher", "John", "Nicholas", "Brandon", "Tyler", "Jonathan", "Christian",
			    "Benjamin", "Nathan", "Samuel", "Jason", "Aaron", "Kevin", "Zachary", "Logan", "Eric", "Cameron",
			    
			    "Mikhail", "Alexei", "Dmitri", "Andrei", "Nikolai", "Viktor", "Pavel", "Maxim", "Sergei", "Yuri",
			    "Ivan", "Vladimir", "Luka", "Dusan", "Marko", "Nikola", "Petar", "Bogdan", "Aleksandar", "Boris",
			    
			    "Stefan", "Igor", "Vladislav", "Filip", "Jovan", "Nenad", "Branimir", "Dragan", "Vuk", "Zoran",
			    "Nemanja", "Miroslav", "Aleksa", "Bogdan", "Goran", "Lazar", "Djordje", "Marko", "Mihajlo", "Nikola",
			    
			    "Lukas", "Paul", "Jonas", "Ben", "Finn", "Leon", "Felix", "Noah", "Luis", "Maximilian",
			    "Julian", "Moritz", "Nico", "Tobias", "Fabian", "Jan", "Tim", "Simon", "Eric", "Matthias",
			    
			    "Louis", "Raphael", "Gabriel", "Adam", "Jules", "Lucas", "Enzo", "Hugo", "Arthur", "Tom",
			    "Théo", "Étienne", "Nathan", "Antoine", "Pierre", "Baptiste", "Édouard", "Quentin", "Benoît", "Maxime",
			    
			    "Leonardo", "Francesco", "Alessandro", "Lorenzo", "Matteo", "Gabriele", "Davide", "Federico", "Marco", "Emanuele",
			    "Riccardo", "Giacomo", "Luca", "Samuele", "Tommaso", "Alessio", "Filippo", "Antonio", "Simone", "Alberto",
			    
			    "Haruto", "Sora", "Ren", "Hiroshi", "Kaito", "Daiki", "Ryo", "Takumi", "Kazuki", "Hikaru",
			    "Tsubasa", "Yuto", "Yamato", "Yuma", "Takeru", "Yuki", "Riku", "Satoshi", "Ryota", "Hayato"
			};

			String[] femaleNamesArray = {
			    
			    "Emma", "Olivia", "Sophia", "Ava", "Isabella", "Mia", "Amelia", "Harper", "Evelyn", "Abigail",
			    "Emily", "Elizabeth", "Sofia", "Avery", "Ella", "Scarlett", "Grace", "Chloe", "Victoria", "Riley",
			    "Aria", "Lily", "Aubrey", "Zoey", "Hannah", "Nora", "Layla", "Eleanor", "Addison", "Brooklyn",
			    
			    "Anastasia", "Natalia", "Svetlana", "Tatiana", "Ekaterina", "Maria", "Anna", "Elena", "Yulia", "Polina",
			    "Masha", "Ksenia", "Alina", "Daria", "Marina", "Oksana", "Ekaterina", "Nina", "Yana", "Larisa",
			    
			    "Jovana", "Milica", "Ana", "Sofija", "Maja", "Jelena", "Nina", "Marija", "Milena", "Andjela",
			    "Katarina", "Jasmina", "Tamara", "Sara", "Bojana", "Ivana", "Sanja", "Teodora", "Vesna", "Zorana",
			    
			    "Lena", "Hannah", "Marie", "Laura", "Emma", "Sophie", "Clara", "Emilia", "Mila", "Lara",
			    "Lea", "Johanna", "Paulina", "Charlotte", "Maja", "Alina", "Isabelle", "Magdalena", "Selina", "Ella",
			    
			    "Léa", "Chloé", "Manon", "Inès", "Camille", "Lola", "Sarah", "Julia", "Clara", "Emma",
			    "Louise", "Alice", "Anna", "Eva", "Lina", "Léna", "Zoé", "Noémie", "Éléna", "Lucie",
			    
			    "Sofia", "Alessia", "Giulia", "Emma", "Martina", "Alice", "Aurora", "Viola", "Ginevra", "Beatrice",
			    "Nicole", "Chiara", "Gaia", "Francesca", "Elena", "Elisa", "Greta", "Bianca", "Anna", "Laura",
			    
			    "Yui", "Aoi", "Rika", "Yuki", "Hina", "Mio", "Sakura", "Nana", "Rin", "Miyu",
			    "Sora", "Hikari", "Yuna", "Aki", "Haruka", "Yuka", "Asuka", "Ayumi", "Ayaka", "Risa"
			};
			
			String[] lastNamesArray = {
				    
				    "Smith", "Johnson", "Williams", "Jones", "Brown", "Davis", "Miller", "Wilson", "Moore", "Taylor",
				    "Anderson", "Thomas", "Jackson", "White", "Harris", "Martin", "Thompson", "Garcia", "Martinez", "Robinson",
				    "Clark", "Rodriguez", "Lewis", "Lee", "Walker", "Hall", "Allen", "Young", "Hernandez", "King",
				    "Wright", "Lopez", "Hill", "Scott", "Green", "Adams", "Baker", "Gonzalez", "Nelson", "Carter",
				    
				    "Ivanov", "Smirnov", "Kuznetsov", "Popov", "Sokolov", "Lebedev", "Kozlov", "Novikov", "Morozov", "Petrov",
				    "Volkov", "Solovyov", "Vasilev", "Zaitsev", "Pavlov", "Semyonov", "Golubev", "Vinogradov", "Bogdanov", "Vorobev",
				   
				    "Jovanovic", "Nikolic", "Petrovic", "Djorđevic", "Stojanovic", "Markovic", "Lukic", "Stankovic", "Pavlovic", "Kostic",
				    "Simic", "Todorovic", "Ilic", "Ristic", "Stevanovic", "Djokovic", "Milosavljevic", "Djuric", "Kovacevic", "Vukovic",
				    
				    "Muller", "Schmidt", "Schneider", "Fischer", "Weber", "Schulz", "Wagner", "Becker", "Hoffmann", "Koch",
				    "Zimmermann", "Braun", "Kruger", "Hofmann", "Klein", "Schröder", "Neumann", "Schwarz", "Lange", "Schmitt",
				    
				    "Martin", "Bernard", "Dubois", "Thomas", "Robert", "Richard", "Petit", "Durand", "Leroy", "Moreau",
				    "Simon", "Laurent", "Lefèvre", "Michel", "Garcia", "David", "Bertrand", "Roux", "Vincent", "Fournier",
				    
				    "Rossi", "Ferrari", "Esposito", "Bianchi", "Romano", "Colombo", "Ricci", "Marino", "Greco", "Bruno",
				    "Conti", "Gallo", "De Luca", "Santoro", "Costa", "Rizzo", "Lombardi", "Barbieri", "Moretti", "Fontana",
				    
				    "Sato", "Suzuki", "Takahashi", "Tanaka", "Watanabe", "Ito", "Yamamoto", "Nakamura", "Kobayashi", "Kato",
				    "Yoshida", "Yamada", "Sasaki", "Yamaguchi", "Matsumoto", "Inoue", "Kimura", "Hayashi", "Shimizu", "Mori"
				};
			
			String[] nationalitiesArray = {
				    
				    "USA", "CAN", "GBR", "AUS", "NZL", "IRL", "ZAF", "IND", "PHL", "SGP",
				    
				    "RUS", "BLR", "UKR", "KAZ", "UZB", "TJK", "KGZ", "ARM", "AZE", "TKM",
				   
				    "SRB", "HRV", "BIH", "MNE", "MKD", "SVN",
				    
				    "FRA", "CAN", "BEL", "CHE", "LUX", "MCO",
				    
				    "ITA", "CHE", "SMR",
				    
				    "JPN"
				};

			
		    maleFirstNames.addAll(Arrays.asList(maleNamesArray));
		    femaleFirstNames.addAll(Arrays.asList(femaleNamesArray));
		    lastNames.addAll(Arrays.asList(lastNamesArray));
		    nationalities.addAll(Arrays.asList(nationalitiesArray));
			
			genders.add(MALE);
			genders.add(FEMALE);

		
	}
	
	public static String generatePassportNumber()
	{
		StringBuilder passportNumber = new StringBuilder();
		
		for(int i = 0; i < PASSPORT_NUMBER_DIGITS; i++)
		{
			passportNumber.append(String.valueOf(rand.nextInt(PASSPORT_NUMBER_MAX_DIGIT + 1)));
		}
		
		return passportNumber.toString();
	}
	
	public static String generateGender()
	{
		int randomIndex = rand.nextInt(genders.size());
		return genders.get(randomIndex);
	}
	
	/**
	 * From the pool of preselected names, selects a random name and combines
	 * with a last name, depending on the wanted gender.
	 * 
	 * @param wantedGender serves for the purpose of selecting whether to generate a 
	 * male or female first name.
	 * @return Full first name + last name with space in between
	 */
	public static String generateFullName(String wantedGender)
	{
		String generatedFirstName;
		String generatedLastName;
		int randomIndex = 0;
		if(wantedGender.equals(MALE))
		{
			randomIndex = rand.nextInt(maleFirstNames.size());
			generatedFirstName = maleFirstNames.get(randomIndex);
		}
		else
		{
			randomIndex = rand.nextInt(femaleFirstNames.size());
			generatedFirstName = femaleFirstNames.get(randomIndex);
		}
		
		randomIndex = rand.nextInt(lastNames.size());
		generatedLastName = lastNames.get(randomIndex);
		
		return generatedFirstName + " " + generatedLastName;
	}
	
	public static String generateNationality()
	{
		int randomIndex = rand.nextInt(nationalities.size());
		return nationalities.get(randomIndex);
	}
}

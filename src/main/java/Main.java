import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.SpeechResult;
import edu.cmu.sphinx.api.StreamSpeechRecognizer;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		if (args.length < 1 || (args.length == 1 && args[0].startsWith("--"))) {
			
			System.out.println("...need the path of audio's to transcribe as argument");
		}
		else {
			
			System.out.println("loading config ...");
			
			String audioPath = "" ;
			//initialize config variables with behavior for english language
			String lang = "en-us" ;
			String modelsPath = "resource:/edu/cmu/sphinx/models" ;
			String modelAcoustic = "en-us";
			String modelDict = "cmudict-en-us.dict";
			String modelLang = "en-us.lm.bin";
			
			if (args.length == 1 || !args[0].startsWith("--")) {
				
				System.out.println("... assuming "+ args[0] +" is the wave audio to transcribe");
				audioPath = args[0];
				System.out.println("... using english language as default for this transcription");
			}
			else {
			
				System.out.println("... assuming "+ args[1] +" is the wave audio to transcribe");
				audioPath = args[1];
				
				if (args[0].contentEquals("--en") || args[0].contentEquals("--english")) {
					//default behavior
					System.out.println("... using english language for this transcription");
				}
				else if (args[0].contentEquals("--fr") || args[0].contentEquals("--french")) {
					
					System.out.println("... using french language for this transcription");
					lang = "fr_fr" ;
					modelsPath = "resource:/tnga/cmu/sphinx/models";
					modelAcoustic = "cmusphinx-fr-5.2";
					modelDict = "fr.dict";
					modelLang = "fr-small.lm.bin";
				}
				else {
					
					System.out.println("xxx unknow language option: "+ args[0]);
					System.out.println("... using english language as default for this transcription");
				}
			}
			
			Configuration configuration = new Configuration();
			
	        configuration.setAcousticModelPath( modelsPath +"/"+ lang +"/"+ modelAcoustic);
	        configuration.setDictionaryPath( modelsPath +"/"+ lang +"/"+ modelDict);
	        configuration.setLanguageModelPath( modelsPath +"/"+ lang +"/"+ modelLang);
	        
	        System.out.println("...done: loading config");
	        
	        StreamSpeechRecognizer recognizer = null;
	        InputStream stream = null;
			try {
				recognizer = new StreamSpeechRecognizer(configuration);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("\nxxx an error ocurred when loading configuration");
			}
			try {
				stream = new FileInputStream(new File(audioPath));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("\nxxx unable to find given audio to transcribe");
			}

			if (recognizer != null && stream != null) {
				System.out.println("start transcription ...\n");
				
				recognizer.startRecognition(stream);
		        SpeechResult result;
		        String sentences = "";
		        while ((result = recognizer.getResult()) != null) {
		        	sentences += result.getHypothesis() + " ";
		            System.out.format("Hypothesis: %s\n", result.getHypothesis());
		            System.out.format("results: %s\n", result.getResult());
		            System.out.format("sentences: %s\n", sentences);
		            
		            //System.out.println("Best 3 hypothesis:");
		            //for (String s : result.getNbest(3)) System.out.println(s);
		        }
		        recognizer.stopRecognition();
		        
		        List<String> lines = Arrays.asList(sentences);
		        Path file = Paths.get("transcribed.txt");
		        try {
					Files.write(file, lines, Charset.forName("UTF-8"));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		        
		        System.out.println("...done: transcription");
		        System.out.println("\n... resulting text was saved in transcribed.txt file ...\n");
			}
		}
	}
}

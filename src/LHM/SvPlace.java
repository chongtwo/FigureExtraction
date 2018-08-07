package LHM;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SvPlace {

	public void svPlaceExtract(String sentence){
		Pattern p = Pattern.compile("SV置于(?<place>.*)见.*?(?<ML1>MeasureLocation#[0-9]+#).*?");
		Matcher m = p.matcher(sentence);
		while (m.find()){
			String place = m.group("place");

		}
	}
}

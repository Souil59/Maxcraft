package fr.maxcraft.utils;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**Warning
*Code from 
*viveleroi
*
*/
public class DurationParser {
	
    public static Long translateTimeStringToDate(String arg_value) {

        Long dateFrom = 0L;

        final Pattern p = Pattern.compile( "([0-9]+)(s|h|m|d|w|d)" );
        final Calendar cal = Calendar.getInstance();

        final String[] matches = preg_match_all( p, arg_value );
        if( matches.length > 0 ) {
            for ( final String match : matches ) {

                final Matcher m = p.matcher( match );
                if( m.matches() ) {

                    if( m.groupCount() == 2 ) {

                        final int tfValue = Integer.parseInt( m.group( 1 ) );
                        final String tfFormat = m.group( 2 );

                        if( tfFormat.equals( "y" ) ) {
                            cal.add( Calendar.YEAR, +1 * tfValue );
                        } else if( tfFormat.equals( "w" ) ) {
                            cal.add( Calendar.WEEK_OF_YEAR, +1 * tfValue );
                        } else if( tfFormat.equals( "d" ) ) {
                            cal.add( Calendar.DAY_OF_MONTH, +1 * tfValue );
                        } else if( tfFormat.equals( "h" ) ) {
                            cal.add( Calendar.HOUR, +1 * tfValue );
                        } else if( tfFormat.equals( "m" ) ) {
                            cal.add( Calendar.MINUTE, +1 * tfValue );
                        } else if( tfFormat.equals( "s" ) ) {
                            cal.add( Calendar.SECOND, +1 * tfValue );
                        } else {
                            return null;
                        }
                    }
                }
            } dateFrom = cal.getTime().getTime();
        }

        return dateFrom;
    }
    public static String[] preg_match_all(Pattern p, String subject) {
		Matcher m = p.matcher(subject);
		StringBuilder out = new StringBuilder();
		boolean split = false;
		while (m.find()) {
			out.append(m.group());
			out.append("~");
			split = true;
		}
		return (split) ? out.toString().split("~") : new String[0];
	}
	public static String translateToString(String string) {
		// TODO Auto-generated method stub
		return string;
	}
}

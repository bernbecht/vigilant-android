package com.br.utils;

import android.content.Context;

import com.parse.Parse;
import com.parse.ParseFacebookUtils;

/**
 * Created by Berhell on 07/08/14.
 */
public class ParseUtils {

    Context context;

    public static void ParseInit(Context c){
        Parse.initialize(c, "zmCTLzchKlxqd3r9ygqYZZYaQwKyzgpvTPhEtO5e", "sktKbehLkB8ukPjTHnVYeYg3kRVPicvviZXQC1kJ");

        ParseFacebookUtils.initialize("684872108258145");
    }
}

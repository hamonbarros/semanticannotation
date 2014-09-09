package br.com.sann.main;

import static java.util.Arrays.asList;

import java.util.LinkedList;
import java.util.List;

import org.annolab.tt4j.TokenHandler;
import org.annolab.tt4j.TreeTaggerWrapper;

public class Example {
        public static void main(String[] args) throws Exception {

        		final List<String> nounsAndAdjectives = new LinkedList<String>(); 
                System.setProperty("treetagger.home", "C:\\Program Files\\TreeTagger");
                TreeTaggerWrapper<String> tt = new TreeTaggerWrapper<String>();
                try {
                        tt.setModel("C:\\Program Files\\TreeTagger\\lib\\english-utf8.par");
                        tt.setHandler(new TokenHandler<String>() {
                                public void token(String token, String pos, String lemma) {
                                        System.out.println(token + "\t" + pos + "\t" + lemma);
                                        if (pos.startsWith("NN") || pos.startsWith("JJ")) {
                                        	nounsAndAdjectives.add(lemma);
                                        }
                                }
                        });
                        tt.process(asList(new String[] {"Hypsographi"}));
                }
                finally {
                        tt.destroy();
                }
                System.out.println(nounsAndAdjectives);
        }
}
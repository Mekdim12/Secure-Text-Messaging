/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.securemessaging;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Random;
// there tow Stages of works needs to ensure the algo is going well
// first just encrypt and also decrypt on sender side and send  the feasbil key combination only
// then after user enters the message and hits the send message then the program checks for the highest bit length among the provded text then teake the bi length
// that are one more level greater than the current highest     

/*
        So the steps are
             1.  First Get The Text what user wants to send 
                1.1 Calculate the bitLength  of each word 
                1.2 the Take the largest bit lenght among them
                1.3 then take from already defined bitlengths which is one more step above  that the largest bit lenght 
                1. outcome  = Suitable BitLength     
Done!  
            2.  Then Generate the neccessary Information needes by the encryption and decryption using the bitlength that been genrated in above steps
                2.1   Encrypt using those provode key 
                2.2   Decrypt using those generated keys
                2.3   Continue this the above steps until the original message and decrypted one is equal 
                2.4   if u have the match then prepare those keys for sending to reciver
Done!
            3. add Other necessary Inforamtion like length count flags recuver phone-number

*/

public class MessageEncryptionLogicLayerFinalVersion2
{
    //               ------------------  Global Vraible Declaration Goes Here -----------
    private static final Random RANDOM_ONE = new Random(System.currentTimeMillis());
    private static final Random RANDOM_TWO = new Random(System.currentTimeMillis() * 10);// Random Number generator as seed for primeInteger Function While Gnerating Keys
    private static final Random RANDOM_THREE = new Random(System.currentTimeMillis());
    private static final Random RANDOM_FOUR = new Random(System.currentTimeMillis() * 100);
    private static final int[] LIST_OF_PREFFERRABLE_BIT_lENGTHS ={64,128,192,256,320,384,448,512,576,640,704,768,832,
                                                                  896,960,1024,1088,1152,1216,1280,1344,1408,1472,1536,
                                                                  1600,1664,1728,1792,1856,1920,1984,2048 }; 

    //                 --------------- End Of Varaible Declaration  -------------
    
    
    
    
    public String messageCiphereCaller(String message){
        return encryptingMessageForSending(message);
    }

    public String messageDeciphering(String cipheredText){
        return decryptingCipheredMessage(cipheredText);
    }




//    public static void main(String[] args) {
        //        String msg = "መአኢድምንቭኡህጅህምህጅምህጅምብንምህጅምጅንምብምጅህምብህንህምንጅምምብብቭብንቭን thifgdfgdfddgsIsTelecommunicationApplication ፍድግድ ህጅግህጅፍግህጅግህጅህምህምጅጅምህብምብንምህፍህግንምቭችግጅ ghjfhgdfgn fgd fgd ghfghf fghhyg ህግፍግፍግህፍትህ ";
        
//        String msg = "mekdim tamirat telecommunicationajhshdgfsjhgsdfsjcdscjhbdb osdfoih dfoidjsfs gdfgdoigdfgdoifgh dofigd";
//        System.out.println(msg);
        
        // TimeMeasure measure = new TimeMeasure();
        // measure.start();
        
//        String cipheredmsg = encryptingMessageForSending(msg);
//
//        System.out.println("\n------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n");

        // System.out.println(cipheredmsg);
        // measure.stop();
        // TimeMeasure measure2 = new TimeMeasure();
        // measure2.start();
            
//        String palinback =decryptingCipheredMessage(cipheredmsg);
//        System.out.println(palinback);
        
// measure2.stop();
//    }
    
    
    //------------------------------------ bit length calculator -------------------------
     public static int LargerFinder(String messsage){
       String [] splitted = messsage.split(" ");
       ArrayList<BigInteger> BitLengthHolder = new ArrayList<>();
       int bitLenghtFinal =0;
       int TempmoraryHolder = 0;
       BigInteger SD = new BigInteger("0");
       for(String word : splitted){
            BigInteger outcome = new BigInteger("0");
            char[] lettersValue = word.toCharArray();
            int exponent = lettersValue.length - 1;
            BigInteger previous_value;
            BigInteger cons_ = new BigInteger("917626");
            for (char letter : lettersValue) {
                previous_value = outcome;
                outcome = new BigInteger("" + (int) letter).multiply(cons_.pow(exponent));
                outcome = outcome.add(previous_value);
                exponent--;
            }
            
           if(TempmoraryHolder < outcome.bitLength()){
                 TempmoraryHolder = outcome.bitLength();
                 SD = outcome;
            }
        
       }
//        System.out.println(SD);
        for(int i=0;i <LIST_OF_PREFFERRABLE_BIT_lENGTHS.length; i++){
           if(!(TempmoraryHolder >= LIST_OF_PREFFERRABLE_BIT_lENGTHS[i])){
               bitLenghtFinal = LIST_OF_PREFFERRABLE_BIT_lENGTHS[i];
               break;
           }
       }
         return bitLenghtFinal;
    }
    
    //----------------------------------- end calcualtor ----------------------------------
    

     
     
     
    //  -----------------------------------------  for Encypting the message methods used :--------------------------
    private static BigInteger[][] beforeAppCheckingGeneratedNumbers2(String message) {
          /*
              This Functions Basically Must be Performed Before The App Opens In order to create feasbile key and without making user waits too much while sending text
          */
          
        int bit_length = LargerFinder(message);
         
        BigInteger ForHoldingSendersInformation[] = {};
        BigInteger ForHoldingReciverInformation[] = {};

        while (true) {// loops forever until it founds the right keys and other information that are neceessary for sending and reciveing that are feasabile
            String sampleMessage = message;
            
            String all_separated_texts[] = sampleMessage.split(" ");
            String plainText = "";
            
            BigInteger senderAllNecessaryInfomration[] = KeyGenerator2(RANDOM_ONE, RANDOM_TWO, true,bit_length); // main variation are here cuz newly values will be taken  from these line
           
            
            BigInteger PublicKeyOFsender = senderAllNecessaryInfomration[0];
            BigInteger PrivateKeyOFsender = senderAllNecessaryInfomration[1];
            BigInteger LargePrimeNumberOFsender = senderAllNecessaryInfomration[2];
            BigInteger EulerToteintOFsender = senderAllNecessaryInfomration[3];

            
            
            BigInteger reciverAllNecessaryInformation[] = KeyGenerator2(RANDOM_THREE, RANDOM_FOUR, false,bit_length);
            BigInteger PublicKeyOfReciver = reciverAllNecessaryInformation[0];
            BigInteger privateKeyOfReciver = reciverAllNecessaryInformation[1];
            BigInteger largePrimeNumbersOFreciver = reciverAllNecessaryInformation[2];
            BigInteger eulerTotientOfReciver = reciverAllNecessaryInformation[3];
            
            

            for (String word : all_separated_texts) {
                
                BigInteger outCome = CalculateAsciiValuesOfSingleWord(word); // returns the asci sum single word
                
                BigInteger ciphered_number = MainEncryptionWhenSending(outCome, PrivateKeyOFsender, LargePrimeNumberOFsender, PublicKeyOfReciver, largePrimeNumbersOFreciver);
                
                
                //  decihphiring 
                
                BigInteger using_decryption_key_of_reciver = ciphered_number.modPow(privateKeyOfReciver, largePrimeNumbersOFreciver); // first using the decrytpionm key of reciver           
                BigInteger using_public_key_of_sender = using_decryption_key_of_reciver.modPow(PublicKeyOFsender, LargePrimeNumberOFsender);
            
                String deciphered = unpackingTheDecipheredNumberValue(using_public_key_of_sender);
                String for_getting_chx[] = deciphered.split("-");
                String msgg = "";
                for (String ss : for_getting_chx) {
                    try {
                        if (ss.equals("")) {
                            msgg = ' ' + msgg;
                            continue;
                        }
                        int val = Integer.parseInt(ss);
                        char s = (char) val;
                        msgg = s + msgg;

                    } catch (NumberFormatException er) {
                        er.printStackTrace();
                    }
                }
                plainText = plainText + msgg + " ";
            }

            if (sampleMessage.trim().equals(plainText.trim())) {
                ForHoldingSendersInformation = senderAllNecessaryInfomration;
                ForHoldingReciverInformation = reciverAllNecessaryInformation;
                break;
            }
        }
        BigInteger ForHoldingAllNecessaryInformation[][] = {ForHoldingSendersInformation, ForHoldingReciverInformation}; // it gets here when it founds the right infn for encryption and decryption
        return ForHoldingAllNecessaryInformation;
    }
   
    private static BigInteger MainEncryptionWhenSending(BigInteger outCome, BigInteger SenderPrivateKey, BigInteger LargePrimeNumberOfSender, BigInteger ReciverPublicKey, BigInteger ReciverLargePrimeNumbers) {
        /*
        this Function Perfroms the Formulas that been provided by Rsa Encryption Algo but in this method encryption will be performed 
        two times two ensures both confidentiality on the message and also the authorizaton of the sender 
           x <- number^privateKey module large number od sender
           x ^ publick key of reciver module reciver of reciver                   then u get ur ciphered text
        */
        
        BigInteger ciphered_number = outCome.modPow(SenderPrivateKey, LargePrimeNumberOfSender); // encyption using  private keys of sender
        ciphered_number = ciphered_number.modPow(ReciverPublicKey, ReciverLargePrimeNumbers); // encrypting using public key of reciver
        
        return ciphered_number;
    }

    private static BigInteger CalculateAsciiValuesOfSingleWord(String word) {
        // this calcaultes the word value using its ascii values
        BigInteger outcome = new BigInteger("0");
        char[] lettersValue = word.toCharArray();
        int exponent = lettersValue.length - 1;
        BigInteger previous_value;
        BigInteger cons_ = new BigInteger("917626"); 
        for (char letter : lettersValue) {
            previous_value = outcome;
            outcome = new BigInteger("" + (int) letter).multiply(cons_.pow(exponent)); // outcome += asciCODE * 917626^exponent
            outcome = outcome.add(previous_value);
            exponent--;
        }

        return outcome;
    }

    private static BigInteger[] KeyGenerator2(Random rand1, Random rand2, boolean Flag,int bitLength) {
        /*
          Generates the basic infn for encryption like
                                 public key 
                                 private key
                                 Euler totient
                                 Large Prime Numbers  
                                 p and q
        */
        int bitlength = bitLength; // if there is any problem of encyption decryption the problem is most likely this

        int encryptionKey_ = 0;
        if (Flag) {
            encryptionKey_ = 2;
        } else {
            encryptionKey_ = 10;
        }
        
        BigInteger p = null;
        BigInteger q = null;

        while (true) {
            p = BigInteger.probablePrime(bitlength, rand1);
            q = BigInteger.probablePrime(bitlength, rand2);
            
            int value = p.compareTo(q);
            
            boolean f = false;
            if (value == 0) {
                f = true;
            }
            
            
       
            if ((q.isProbablePrime(bitlength) && p.isProbablePrime(bitlength)) && !f) {
                break;
            }
            
        }

        BigInteger n = p.multiply(q);

        
        BigInteger EulerTotient = (p.subtract(new BigInteger("1")).multiply(q.subtract(new BigInteger("1"))));

        while (true) {
            /*
            so start with encrypotion key 2 cuz  1 < 2 
            
            1 < encryption key < Euler Totient
            
            
            GCD(EulerTotient, Encryption key ) == 1 means the common divisor of euler totient and the key is necessroyly needs to be 1 so keep altering encryption key until
            this condition fulfills
            
            
            */
            
            BigInteger result = EulerTotient.gcd(new BigInteger("" + encryptionKey_));  


            int checking_value = result.compareTo(BigInteger.ONE);

            if (checking_value == 0) {
                break;
            } else {
                encryptionKey_++;
            }
            
        }

        BigInteger encryptionKey = new BigInteger("" + encryptionKey_);
        
        // incerse of encryption key times with mod of euler tottient
        
        // Decryption Key = INVESRE(encryptionkEY) mod(EulerTotient)
        
        BigInteger DecrytptionKey = encryptionKey.modInverse(EulerTotient);
        
        
        BigInteger[] ret = {encryptionKey, DecrytptionKey, n, EulerTotient};
        return ret;
    }

    public static String cipheringTheNumberToVigiousCharacter2(BigInteger value) {
        BigInteger divisor = new BigInteger("917626");
        String ss = "";
        String finalRepresentationOfCipherText = "";
        BigInteger Holder = null;
        
        while (true) {
            Holder = value.divide(divisor);
            BigInteger remainder = value.subtract(Holder.multiply(divisor));
            ss +=  remainder+"-";
            if (Holder.compareTo(divisor) >0) {
                value = Holder;
            } else {
                break;
            }
        }
        
        String mult = doer(Holder);
        
        String rema [] =ss.split("-");
         
         String remai = "";
         for(String t : rema){
             if(t.trim().equals(""))
                 continue;
             remai +=doer(new BigInteger(t))+"𐐷";   // inverted a aalike
             
        }
        
        finalRepresentationOfCipherText = mult+"ⱍ"+remai; // sigma alike
        
        return finalRepresentationOfCipherText;
    }
    
    private static String doer(BigInteger value){
        
        BigInteger divisor = new BigInteger("256");
        String ss = "";
        BigInteger Holder = null;
        String for_return = "";
        
        
        while (true) {
            Holder = value.divide(divisor);
            BigInteger remainder = value.subtract(Holder.multiply(divisor));
            ss +=CharacterEncodingInUserDefinedMap(remainder);
            if (Holder.compareTo(divisor.subtract(new BigInteger("1"))) >0) {
                value = Holder;
            } else {
                break;
            }
        }
        
        for_return = CharacterEncodingInUserDefinedMap(Holder)+"𝝵"+ss; // zigzag alike
        return for_return;
    }
  
    public static String ciphering_Keys(BigInteger PublicKeyOFSender, BigInteger publicKeyOfReciver, BigInteger EuletTotientOfReciver, BigInteger largePrimeNumbersOfRecivers, BigInteger largePrimeNummbersOfSender) {
        /*
           This method used to cipher secret keys and other super necessary information for dcryptiong after its been sent
        */
        String eqx = "⋯";
        BigInteger Number = new BigInteger("256");

        if (PublicKeyOFSender.compareTo(Number) > 0) {
            eqx += divider_method_for_ciphering_keys(PublicKeyOFSender) + "·";
        } else {
            eqx += "" + CharacterEncodingInUserDefinedMap(PublicKeyOFSender) + "·";
        }

        if (publicKeyOfReciver.compareTo(Number) > 0) {
            eqx += divider_method_for_ciphering_keys(publicKeyOfReciver) + "·";
        } else {
            eqx += "" + CharacterEncodingInUserDefinedMap(publicKeyOfReciver) + "·";
        }

        if (EuletTotientOfReciver.compareTo(Number) > 0) {
            eqx += divider_method_for_ciphering_keys(EuletTotientOfReciver) + "·";
        } else {
            eqx += "" + CharacterEncodingInUserDefinedMap(EuletTotientOfReciver) + "·";
        }

        if (largePrimeNumbersOfRecivers.compareTo(Number) > 0) {
            eqx += divider_method_for_ciphering_keys(largePrimeNumbersOfRecivers) + "·";
        } else {
            eqx += CharacterEncodingInUserDefinedMap(largePrimeNumbersOfRecivers) + "·";
        }
        if (largePrimeNummbersOfSender.compareTo(Number) > 0) {
            eqx += divider_method_for_ciphering_keys(largePrimeNummbersOfSender) + "⋯";
        } else {
            eqx += CharacterEncodingInUserDefinedMap(largePrimeNummbersOfSender) + "⋯";
        }

        return eqx;
    }

    public static String divider_method_for_ciphering_keys(BigInteger number) {
        /*
           For dividing values of the keys holding its values like message like base to remainder values
        */
        BigInteger Holder = null;
        String ss = "";
        String finalRepresentationOfCipherText = "";
        BigInteger divisor = new BigInteger("256");
        int count = 1;
        while (true) {
            Holder = number.divide(divisor);
            BigInteger remainder = number.subtract(Holder.multiply(divisor));
            ss = CharacterEncodingInUserDefinedMap(remainder) + ss;

            if (Holder.compareTo(divisor.subtract(new BigInteger("1"))) > 0) {
                count++;
                number = Holder;
            } else {
                break;
            }
        }
        finalRepresentationOfCipherText += "" + CharacterEncodingInUserDefinedMap(Holder) + ss;

        return finalRepresentationOfCipherText;
    }

    private static String encryptingMessageForSending(String message) {
         /*
          This the method u are going to call when u want to send the message  will return ciphered text 
         */
        BigInteger PublicKeyOFsender = null;
        BigInteger PrivateKeyOFsender = null;
        
        
        BigInteger LargePrimeNumberOFsender = null;
        BigInteger EulerToteintOFsender = null;
        
        BigInteger PublicKeyOfReciver = null;
        
        
        BigInteger largePrimeNumbersOFreciver = null;
        BigInteger eulerTotientOfReciver = null;

        BigInteger neccessaryInformation[][] = beforeAppCheckingGeneratedNumbers2(message);
        
        int count = 1; 
        for (BigInteger[] currentValue : neccessaryInformation) {
            for (int i = 0; i < currentValue.length; i++) {
                if (count == 1) {
                    PublicKeyOFsender = currentValue[0];
                    PrivateKeyOFsender = currentValue[1];
                    LargePrimeNumberOFsender = currentValue[2];
                    EulerToteintOFsender = currentValue[3];
                } else if (count == 2) {
                    PublicKeyOfReciver = currentValue[0];
                    largePrimeNumbersOFreciver = currentValue[2];
                    eulerTotientOfReciver = currentValue[3];
                }
            }
            count++;
        } 
        
        String all_separated_texts[] = message.split(" ");
        String finalCipheredTextToBeSend = "";

        for (String word : all_separated_texts) {
            BigInteger outCome = CalculateAsciiValuesOfSingleWord(word);
       
//            System.out.println(outCome);
            BigInteger ciphered_number = MainEncryptionWhenSending(outCome, PrivateKeyOFsender, LargePrimeNumberOFsender, PublicKeyOfReciver, largePrimeNumbersOFreciver);
//      System.out.println("Ciphered Number="+ciphered_number);
            String cipheredText =cipheringTheNumberToVigiousCharacter2(ciphered_number);
            finalCipheredTextToBeSend += cipheredText + "῾";
        }
        
        System.err.println(finalCipheredTextToBeSend);
        String PatternOfCipheredKeys = ciphering_Keys(PublicKeyOFsender, PublicKeyOfReciver, eulerTotientOfReciver, largePrimeNumbersOFreciver, LargePrimeNumberOFsender);
        finalCipheredTextToBeSend = PatternOfCipheredKeys + finalCipheredTextToBeSend;
        
        return finalCipheredTextToBeSend;
    }

//  -------------------------------------------------End --------------------------------------------------------
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
//  --------------------------------- for Decryption the ciphered message methods used are:---------------------------------------------
    
  private static String gettingLettersTogetherForMakingWord(String decipheredText) {
      /*
          after the number is deciphered on the reciver it needs to be mapped backed to the character and creates this what this method do's 
      */
        String msg = "";
        String for_getting_chx[] = decipheredText.split("-");
        for (String ss : for_getting_chx) {
            try {
                if (ss.equals("")) {
                    msg = ' ' + msg;
                    continue;
                }
                int val = Integer.parseInt(ss);
                char s = (char) val;
                msg = s + msg;

            } catch (NumberFormatException er) {
                er.printStackTrace();
            }
        }
        return msg;
    }

    private static String mainMessageDecrypter(String word, BigInteger PrivateKeyofReciver, BigInteger largeNumberOfReciver, BigInteger publicKeyOfSender, BigInteger largeNumberOfSender) {
        /*
          in this function the main decryption performs usng famous formulas provided in Rsa Encryption Algo 
        */
        BigInteger mainValue = forReturningActualNumberValueFromCipheredText2(word);
//        System.out.println("Main Values="+mainValue);
        BigInteger using_decryption_key_of_reciver = mainValue.modPow(PrivateKeyofReciver, largeNumberOfReciver); // first using the decrytpionm key of reciver           
        BigInteger using_public_key_of_sender = using_decryption_key_of_reciver.modPow(publicKeyOfSender, largeNumberOfSender);
//        System.out.println(using_public_key_of_sender);
        


        String decipheredText = unpackingTheDecipheredNumberValue(using_public_key_of_sender);
        
        String singleWordMessage = gettingLettersTogetherForMakingWord(decipheredText);
//        System.out.println(singleWordMessage);
        return singleWordMessage;
    }

    public static String unpackingTheDecipheredNumberValue(BigInteger value) {
        String values = "";
        while (true) {// this like finding binary numbr or converting decimal to binary  
            BigInteger divided_value = value.divide(new BigInteger("917626"));
            BigInteger reaminder = divided_value.multiply(new BigInteger("917626"));
            BigInteger unpacked_value = value.subtract(reaminder);

            
                values += unpacked_value + "-";
                if (reaminder.compareTo(new BigInteger("917626")) < 0) {
                    break;
                } else {
                    value = divided_value;
                }
            

        }
        
        return values; // all remaindrers
    }

    private static String decryptingCipheredMessage(String CipheredText) {
        /*
            This is the method u gonna call when there is a message that needs to be Decrypted
        */
        String plainText = "";
        int starting_index = CipheredText.indexOf('⋯') + 1;
        int last_index = CipheredText.lastIndexOf('⋯');
        String key_combination = CipheredText.substring(starting_index, last_index);  // finding keys from the message 
        
        BigInteger[] neccessaryInformationForDecryptingCipheredText = decryptingKeysFromCipheredText(starting_index, last_index, key_combination);

        BigInteger publick_key_sender = neccessaryInformationForDecryptingCipheredText[0];
        BigInteger public_key_reciver = neccessaryInformationForDecryptingCipheredText[1];
        BigInteger eulerTotientofReciver = neccessaryInformationForDecryptingCipheredText[2];
        BigInteger largePrimeNumbersOFReciver = neccessaryInformationForDecryptingCipheredText[3];
        BigInteger largePrimeNumbersOFSender = neccessaryInformationForDecryptingCipheredText[4];

        
        BigInteger privateKey = public_key_reciver.modInverse(eulerTotientofReciver); // decryption key of reciver 
        String main_message_only = CipheredText.substring(last_index + 1);// getting all messages except for keys
        String[] single_words_from_actual_message = main_message_only.split("῾"); // message only 
        for (String word : single_words_from_actual_message) {
            String wordMessage = mainMessageDecrypter(word, privateKey, largePrimeNumbersOFReciver, publick_key_sender, largePrimeNumbersOFSender); // main decrypter
            plainText = plainText + wordMessage + " ";
        }
        
        return plainText;
    }

    public static BigInteger[] decryptingKeysFromCipheredText(int starting_index, int last_index, String key_combination) {
        String individual_key[] = key_combination.split("·");
        int round_counter = 0;
        BigInteger publickKey_of_sender = null, publick_key_of_reciver = null, Eulertotient = null, largeNumber = null, LargerPrimeNumbersOFSender = null;
        for (String key : individual_key) {
            if (key.length() == 1 && round_counter == 0) {
                publickKey_of_sender = new BigInteger("" + IndexReturningFromUserDefinedMap(key));
            } else if (key.length() == 1 && round_counter == 1) {
                publick_key_of_reciver = new BigInteger("" + IndexReturningFromUserDefinedMap(key));
            } else if (key.length() != 1 && round_counter == 2) {
                Eulertotient = commonDividerMethodForDecryptingKeys(key);
            } else if (key.length() != 1 && round_counter == 3) {
                largeNumber = commonDividerMethodForDecryptingKeys(key);
            } else if (key.length() != 1 && round_counter == 4) {
                LargerPrimeNumbersOFSender = commonDividerMethodForDecryptingKeys(key);
            } else {
                System.out.println("have no idea if it gets here");
            }
            round_counter++;
        }
        
        BigInteger ret[] = {publickKey_of_sender, publick_key_of_reciver, Eulertotient, largeNumber, LargerPrimeNumbersOFSender};
        return ret;
    }

    private static BigInteger commonDividerMethodForDecryptingKeys(String key) {
        long actual_number_ = IndexReturningFromUserDefinedMap("" + key.charAt(0));
        BigInteger actual_number = new BigInteger("" + actual_number_);

        String indexs = key.substring(1);
        String number_patterm = "";

        for (int i = 0; i < indexs.length(); i++) {
            number_patterm += IndexReturningFromUserDefinedMap("" + indexs.charAt(i)) + "⏑";    // after separating the numbers ⏑ this ch then u can use it to multiply it later
        }
        try {
            String s[] = number_patterm.split("⏑");
            for (String indv : s) {
                BigInteger parsed_number = new BigInteger(indv);
                actual_number = actual_number.multiply(new BigInteger("256")); // if neccessary use absolute function
                actual_number = actual_number.add(parsed_number);
            }
        } catch (NumberFormatException er) {
            System.out.println("Damn bro u must be missingin something cuz this ain should be gettign here");
        }
        return actual_number;
    }
    
    private static BigInteger forReturningActualNumberValueFromCipheredText2(String word) {
        String equation [] = word.split("ⱍ"); // splitting Main Base and Main Remainders 
        
        int round =1;
        
        String oneStepBackEqx;
        
        String for_holding_base_value_up_level = "";
        String for_holding_up_level_remainders = "";
        
        for(String singleWord : equation){
            if(round == 1){
                String values [] = singleWord.split("𝝵");
                String baseValue = values[0];
                String remainders = values[1];
                String ss= "";
                BigInteger BaseValue =new BigInteger(""+IndexReturningFromUserDefinedMap(baseValue)); // will check for sign and get the index then plaxe it in biginteger value
                BigInteger multiPlier = new BigInteger("256");
                BigInteger TotalValue;
                
                for(int i=remainders.length()-1;i>=0;i--){
                     BigInteger index_value_of_curren_char =new BigInteger(""+IndexReturningFromUserDefinedMap(""+remainders.charAt(i)));
                     BigInteger val = BaseValue.multiply(multiPlier);
                     TotalValue = val.add(index_value_of_curren_char);
                     BaseValue = TotalValue;
                }
                for_holding_base_value_up_level +=BaseValue+"-";
                  
            }else if(round == 2){
                String[] single_values = singleWord.split("𐐷"); // with inverted A separating individual remainders low level
                for(String single_remainder_Values_base_pair : single_values){
                    String values_separated[] = single_remainder_Values_base_pair.split("𝝵"); // separating separating each raminder to its base and internal remainders
                    String base_number = values_separated[0];
                    String remainders_only = values_separated[1];
                    BigInteger BaseValue = new BigInteger(""+IndexReturningFromUserDefinedMap(base_number));
                    BigInteger MultiPlier = new BigInteger("256");
                    BigInteger TotalValues = new BigInteger("0");
                    
                    for(int i = remainders_only.length()-1; i>=0;i--){
                     BigInteger index_value = new BigInteger(""+IndexReturningFromUserDefinedMap(""+remainders_only.charAt(i)));
                     BigInteger curr_val = BaseValue.multiply(MultiPlier);
                     TotalValues = curr_val.add(index_value);
                     BaseValue = TotalValues;
                    }
                    
                   for_holding_up_level_remainders += BaseValue+"-"; 
                   
                }
            }
            round ++;
        }
        
        oneStepBackEqx = for_holding_base_value_up_level + for_holding_up_level_remainders;
        String Final_Separation [] =oneStepBackEqx.split("-");
        
        String base_value = Final_Separation[0];
        BigInteger MainBaseValue = new BigInteger(base_value);
        
          
        BigInteger multiplier = new BigInteger("917626");
        BigInteger TotalVal;
        
        
        for(int j=Final_Separation.length-1;j>0;j--){
            
            BigInteger current_index_remainder = new BigInteger(Final_Separation[j]);
            BigInteger current_value = MainBaseValue.multiply(multiplier);
            TotalVal = current_value.add(current_index_remainder);
            MainBaseValue = TotalVal;
            
        }
        return MainBaseValue;
    }

// ---------------------------------------------------------------- End ----------------------------------------------------------------------
    
    
    
    
    
    
    
    
    // --------------------------------------------------- Common Methods Containes User Defined Maps Of Encoding-----------------------------
    
    public static char CharacterEncodingInUserDefinedMap(BigInteger index) {
        final String SYMBOL = "QWERTYUIOPASDFGHJKLZXCVBNMqwertyuiopasdfghjklzxcvbnm012345689ሀሁሂሃሄህሆለሉሊላሌልሎሐሑሒሔሕ `-=][\';/.,~!@#$%^&*()_+|}{:\"><?መሙሚማምሞሱሲሳሴሶረሩሪራሬ፣።ሸሹሺሼሽቀቁቂቃበቡቤብቲታቴትቶቸቹቺቻችቾነኑኒናኔንኖኘኙኚኜኝß►╢♀Ö⌐☻☼♂♠♣♥♦♪♫◄↓↔7αâàî£ùÑº¬½¼¡╞╦¢╠▄ÿ∩εφ↨ΩΘσΣπ▀┌┘ፑፒፓፔፕፖ╘╙╤╨▐╚┼├┬┴└╜║╖◙☺¿ªñጠጡጢጤጥዕí፩፪፫፬፭፮፯፰፱";
        char c = 0;
        try {
            long valu = index.longValue(); // longvalueExact() was the promising function but it requires android verion s or 12

            int Index = Integer.parseInt("" + valu);

            c = SYMBOL.charAt(Index);
        } catch (NumberFormatException er) {
            er.printStackTrace();
        }

        return c;
    }

    public static int IndexReturningFromUserDefinedMap(String chx) {
        final String SYMBOL = "QWERTYUIOPASDFGHJKLZXCVBNMqwertyuiopasdfghjklzxcvbnm012345689ሀሁሂሃሄህሆለሉሊላሌልሎሐሑሒሔሕ `-=][\';/.,~!@#$%^&*()_+|}{:\"><?መሙሚማምሞሱሲሳሴሶረሩሪራሬ፣።ሸሹሺሼሽቀቁቂቃበቡቤብቲታቴትቶቸቹቺቻችቾነኑኒናኔንኖኘኙኚኜኝß►╢♀Ö⌐☻☼♂♠♣♥♦♪♫◄↓↔7αâàî£ùÑº¬½¼¡╞╦¢╠▄ÿ∩εφ↨ΩΘσΣπ▀┌┘ፑፒፓፔፕፖ╘╙╤╨▐╚┼├┬┴└╜║╖◙☺¿ªñጠጡጢጤጥዕí፩፪፫፬፭፮፯፰፱";
        return SYMBOL.indexOf(chx);
    }

    //------------------------------------------------------------------ End  ----------------------------------------------------------------


  
  


}







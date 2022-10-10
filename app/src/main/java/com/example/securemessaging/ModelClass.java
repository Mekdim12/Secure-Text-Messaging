package com.example.securemessaging;

public class ModelClass {


        private int imageview;
        private String textview1;
        private String textview2;
        private String textview3;
        //new code
        private String divider;
        private String id;



        ModelClass (int imageview ,String textview1,String textview2,String textview3,String id,String divider)
        {
            this.imageview=imageview;
            this.textview1=textview1;
            this.textview2=textview2;
            this.textview3=textview3;
            this.divider=divider;
            this.id = id;
        }

        public int getImageview() {
            return imageview;
        }

        public String getTextview1() {
            return textview1;
        }

        public String getDivider()
        {
            return divider;
        }

        public String getTextview2() {
            return textview2;
        }

        public String getTextview3() {
            return textview3;
        }

        public String getId() {
            return id;
        }
}


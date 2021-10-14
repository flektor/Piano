bool isTrue = false;

void setup() {
    pinMode(13, OUTPUT);
    digitalWrite(13,LOW);
    for(int i = A0; i< A3; i++){ 
        pinMode(i, INPUT);  
    }
}

void loop() {
    for(int i = A0; i< A3; i++){
        float value = analogRead(i) * 0.001;  
        if(value > 0.3){
            String str = "0" + String(i-A0) + "=" + String(value);
            Serial.begin(9600);
            Serial.print(str);
            Serial.end();
            isTrue = true;    
        }
    }
    if(isTrue) {
        isTrue = false;
    }
}

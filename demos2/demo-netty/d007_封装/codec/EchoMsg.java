package com.danger.demo.d007_封装.codec;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Getter @Setter
public class EchoMsg implements Serializable {

    private String messageId; // 长度32
    private String payload;   // 变长

    public EchoMsg(){
        this.messageId = UUID.randomUUID().toString().replace("-", "");
    }

    public EchoMsg(String payload) {
        super();
        this.payload = payload;
    }

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            System.out.println(UUID.randomUUID().toString().replace("-", "").length());
        }
    }

    @Override
    public String toString() {
        return "EchoMsg{" +
                "messageId='" + messageId + '\'' +
                ", payload='" + payload + '\'' +
                '}';
    }
}

package com.j2ck.dict;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result {
    private String binyan;
    private String present1;
    private String present2;
    private String past1;
    private String past2;
    private String future1;
    private String future2;
    private String translation;
    private String infinitiv;

    @Override
    public String toString() {
        return "Result{" +
                "binyan='" + binyan + '\'' +
                ", present1='" + present1 + '\'' +
                ", present2='" + present2 + '\'' +
                ", past1='" + past1 + '\'' +
                ", past2='" + past2 + '\'' +
                ", future1='" + future1 + '\'' +
                ", future2='" + future2 + '\'' +
                ", translation='" + translation + '\'' +
                ", infinitiv='" + infinitiv + '\'' +
                '}';
    }
}

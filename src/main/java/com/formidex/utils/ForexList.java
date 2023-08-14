package com.formidex.utils;

import com.formidex.service.Forex;
import lombok.Data;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.URI;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Component
@Data
public class ForexList {
    /**
     * Bean for fetching the data from the link,unziping and creating objects in memory for computation
     * @return the list of objects for computation
     */
    @Bean
    public List<Forex> getData(){
        URI uri=URI.create("https://www.ecb.europa.eu/stats/eurofxref/eurofxref-hist.zip");
        List<Forex>forexList=new ArrayList<>();
            try (InputStream in = uri.toURL().openStream();
                 ReadableByteChannel rbc = Channels.newChannel(in);
                 FileOutputStream fos = new FileOutputStream("currency.csv");) {
                fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
                BufferedInputStream bis = new BufferedInputStream(uri.toURL().openStream());//stream for deserializing the file from network
                ZipInputStream zip=new ZipInputStream(bis);
                ZipEntry entry=zip.getNextEntry();
                String lines=new String(zip.readAllBytes());
                //System.out.println(lines);
                String[]strings=lines.split("\n");
                for (String string : strings) {
                    if (string.toLowerCase().contains("date"))
                        continue;
                    String[]values=string.split(",");

                    int i=0;
                    try {
                        Forex forex = Forex.builder()//creating objects from the file
                                .date(LocalDate.parse(values[i]))
                                .uSD(values[++i]).jPY(values[++i])
                                .bGN(values[++i]).cYP(values[++i])
                                .cZK(values[++i]).dKK(values[++i])
                                .eEK(values[++i]).gBP(values[++i])
                                .hUF(values[++i]).lTL(values[++i])
                                .lVL(values[++i]).mTL(values[++i])
                                .pLN(values[++i]).rOL(values[++i])
                                .rON(values[++i]).sEK(values[++i])
                                .sIT(values[++i]).sKK(values[++i])
                                .cHF(values[++i]).iSK(values[++i])
                                .nOK(values[++i]).hRK(values[++i])
                                .rUB(values[++i]).tRL(values[++i])
                                .tRY(values[++i]).aUD(values[++i])
                                .bRL(values[++i]).cAD(values[++i])
                                .cNY(values[++i]).hKD(values[++i])
                                .iDR(values[++i]).iLS(values[++i])
                                .iNR(values[++i]).kRW(values[++i])
                                .mXN(values[++i]).mYR(values[++i])
                                .nZD(values[++i]).pHP(values[++i])
                                .sGD(values[++i]).tHB(values[++i])
                                .zAR(values[++i])
                                .build();
                        forexList.add(forex);
                    }catch (Exception e){
                        System.err.println(string);
                    }
                }
                return forexList;
            }catch (Exception e){
                e.printStackTrace();
                return new ArrayList<>();
            }
    }
}

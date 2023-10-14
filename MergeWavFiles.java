import javax.sound.sampled.*;
import java.io.*;
import java.util.*;

public class MergeWavFiles {
    public static void main(String[] args) {
        String inputFile1 = "input1.wav";
        String inputFile2 = "input2.wav";
        String outputFile = "output.wav";

        // Add your audio(.wav) list here
        List<String> audioList = new ArrayList<String>();
        audioList.add(inputFile1);
        audioList.add(inputFile2);
        audioList.add("input3.wav");

        
        // mergeAudio(inputFile1, inputFile2,outputFile);
        try {
            mergeAudioList(audioList, outputFile);
        }
        catch(SingleAudioToMergeException e){
            System.out.println(e);
        }
        

        
    }

    static void mergeAudioList(List<String> audioList, String outputFile) throws SingleAudioToMergeException  {
        int audioListSize = audioList.size();
        String helperWavFile = "helper.wav";
        if(audioListSize == 1){
            throw new SingleAudioToMergeException("Single audio is not able to merge");    
        }
        mergeAudio(audioList.get(0), audioList.get(1), outputFile);
        
        for(int index = 2; index < audioListSize; index++){
            try {

                // Create input and output streams
                FileInputStream inputStream = new FileInputStream(outputFile);
                FileOutputStream outputStream = new FileOutputStream(helperWavFile);
    
                // Create a buffer to read and write data
                byte[] buffer = new byte[1024];
                int bytesRead;
    
                // Read from the source file and write to the destination file
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
    
                // Close the streams
                inputStream.close();
                outputStream.close();
    
            } catch (IOException e) {
                e.printStackTrace();
            }

            mergeAudio(helperWavFile, audioList.get(2), outputFile);

            File file = new File(helperWavFile);
            file.delete();
        }
    }

    static void mergeAudio(String inputFile1, String inputFile2, String outputFile){
        System.out.println("Merging "+inputFile1+" to "+outputFile);
        try {
            AudioInputStream audio1 = AudioSystem.getAudioInputStream(new File(inputFile1));
            AudioInputStream audio2 = AudioSystem.getAudioInputStream(new File(inputFile2));

            // Get the format of the first input file
            AudioFormat format = audio1.getFormat();

            // Calculate the total length of the merged audio
            long length = audio1.getFrameLength() + audio2.getFrameLength();

            // Create an audio input stream that concatenates both files
            AudioInputStream audioStream = new AudioInputStream(new SequenceInputStream(audio1, audio2), format, length);

            // Write the merged audio to the output file
            AudioSystem.write(audioStream, AudioFileFormat.Type.WAVE, new File(outputFile));

            System.out.println("Files merged successfully.");
        } catch (UnsupportedAudioFileException | IOException e) {
            e.printStackTrace();
        }
    }
}

class SingleAudioToMergeException extends Exception  
{  
    public SingleAudioToMergeException (String str)  
    {  
        // calling the constructor of parent Exception  
        super(str);  
    }  
}
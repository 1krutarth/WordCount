package wordcount;

import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;

public class WordCount {
	
  public static class TokenizerMapper extends Mapper<Object, Text, Text, IntWritable>{
	  private Text word = new Text();

	  public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
	    	String fname = ((FileSplit) context.getInputSplit()).getPath().getName();

	    	
	    	String val = value.toString();
	    	int cntr = 0;
	    	
	    	// education
	    	Pattern p = Pattern.compile("[^#/\"-+_]([eE]ducation)[^-\"al]");
	    	Matcher m = p.matcher(val);
	    	while( m.find() ){
	    		cntr += 1;
	    	}
	    	word.set(fname+":education");
	    	context.write( word, new IntWritable(cntr) );
	    	cntr = 0;
	    	
	    	// agriculture
	    	Pattern p1 = Pattern.compile("[^#/\"-+_]([aA]griculture)[^-\"al]");
	    	Matcher m1 = p1.matcher(val);
	    	while( m1.find() ){
	    		cntr += 1;
	    	}
	    	word.set(fname+":agriculture");
	    	context.write( word, new IntWritable(cntr) );
	    	cntr = 0;
	    	
	    	// sports
	    	Pattern p2 = Pattern.compile("[^#/\"-+_]([sS]ports)[^-\"al]");
	    	Matcher m2 = p2.matcher(val);
	    	while( m2.find() ){
	    		cntr += 1;
	    	}
	    	word.set(fname+":sports");
	    	context.write( word, new IntWritable(cntr) );
	    	cntr = 0;
	    	
	    	// politics
	    	Pattern p3 = Pattern.compile("[^#/\"-+_]([pP]olitics)[^-\"al]");
	    	Matcher m3 = p3.matcher(val);
	    	while( m3.find() ){
	    		cntr += 1;
	    	}
	    	word.set(fname+":politics");
	    	context.write( word, new IntWritable(cntr) );
	    	cntr = 0;
	    	
	    	}
	    }


  public static class IntSumReducer extends Reducer<Text,IntWritable,Text,IntWritable> {
	  private IntWritable result = new IntWritable();
	  public void reduce(Text key, Iterable<IntWritable> values, Context context ) throws IOException, InterruptedException {
	  int sum = 0;
      for (IntWritable val : values) {
        sum += val.get();
      }
      result.set(sum);
      context.write(key, result);
    }
  }

  // split the key using : as delimiter

  public static void main(String[] args) throws Exception {
	  Configuration conf = new Configuration();
	  Job job = Job.getInstance(conf, "word count");
	  job.setJarByClass(WordCount.class);
	  job.setMapperClass(TokenizerMapper.class);
	  job.setCombinerClass(IntSumReducer.class);
	  job.setReducerClass(IntSumReducer.class);
	  job.setOutputKeyClass(Text.class);
	  job.setOutputValueClass(IntWritable.class);
	  FileInputFormat.addInputPath(job, new Path(args[0]));
	  FileOutputFormat.setOutputPath(job, new Path(args[1]));
	  System.exit(job.waitForCompletion(true) ? 0 : 1);
	  }
  }
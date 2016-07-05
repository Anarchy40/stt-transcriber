>CMU's sphinx usage example for automatic audio transcription

This hack was made only for experimentation, learning and testing purpose. The gold was to show how CMU's sphinx library can be use for audio transcription (recorded speech to text) job.

The library only support for now **ms-wave** audio format (caracteristics:`16khz, 16bit, mono` are recommended for good transcription).

>This project was build on eclipse luna with *buildship* (gradle) integration plugin

#### actual usage
assuming *cwd* is the project directory
```sh
$ java -jar build/libs/angel_transcriber-all.jar [--language] "path-to-wave-audio"
```
*where [--language] is an optional language setting option: (supported: `--en` or `--english`, `--fr` or `--french`)*.

More language's data models can be downloaded [here](https://sourceforge.net/projects/cmusphinx/files/Acoustic%20and%20Language%20Models/). For largest speech recognation, use when available, big language model instead of small one (*for example `fr.lm.dmp` instead of `fr-small.lm.bin`*).

#### gradle
this is to generate the jar file
```sh
$ gradle fatjar 
```

**todo**: organize transcribed text function of speakers; noise audio filter.
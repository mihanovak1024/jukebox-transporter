# jukebox-transporter

A program that downloads songs from Youtube based on Google Spreadsheet entries and uploads them to Google Drive in mp3 format.

## Use case
If a song comes up somewhere and you definitely want it on your phone/laptop, but you'll probably forget about it before you get to a computer. This program lets you insert a song name into your Google Spreadsheet which will later be scanned and the song will be uploaded to your Google Drive. 

## Base logic
The entry of your Google Spreadsheet is specified by an artist and a song name (can be just a song name in case you don't know the artist). Once a day/week (or as a manual user trigger) the program "scans" the specified Google Spreadsheet, recommends you the song from Youtube and in when you approve the recommended song is the one you wanted, it gets listed as ready-to-download. Later it gets downloaded from Youtube, converted from mp4 to mp3 and gets uploaded to your Google Drive, ready for use on your mobile or any other device that can use Google Drive.

After the song gets downloaded locally (to your device), it gets deleted from Google Drive (to save space) and a song info entry is made in the Google Spreadsheet downloaded-song backlog.


var ChoosenPodacast; //hold the Chosen podcast
var ChoosenEpisode;//hold the Chosen podcast episode
var AllTrascribes;
/**
 * this function is an ajax GET call to Server to search for podcast
 * **/
function handleSubmitSearch() {
    var loader = document.getElementById("loader");
    if(document.getElementById("SearchBox").value != "") {
        loader.style.display = "";
        var reqData = {"data": document.getElementById("SearchBox").value};//get input from user
        $.ajax({
            type: "GET",
            contentType: 'application/json',
            //contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
            url: "/PodcastsSpeechToText/itunesSearch",
            data: reqData,
            success: function (response) {
                loader.style.display = "none";
                HandleResponse(response);
            },
            error: function (response) {
                loader.style.display = "none";
                window.alert("Something Went Wrong : " + response.status + " " + response.statusText);
            }
        });
    }
    else
    {
        alert("You tried to fool me? joke on you!! Plaese enter a real podcast name");
    }
}

/**
 * this function is an ajax GET call to Server to search for podcast in the pool of Transcription podcasts
 * **/
function handleSubmitTextSearch() {
    var loader = document.getElementById("loader");
    if(document.getElementById("SearchBox").value != "") {
        loader.style.display = "";
        var reqData = {"data": document.getElementById("SearchBox").value}; //get input from user
        $.ajax({
            type: "GET",
            contentType: 'application/json',
            //contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
            url: "/PodcastsSpeechToText/luceneSearch",
            data: reqData,
            success: function (response) {
                loader.style.display = "none";
                luceneSearchResponse(response);
            },
            error: function (response) {
                loader.style.display = "none";
                window.alert("Something Went Wrong : " + response.status + " " + response.statusText);
            }
        });
    }
    else
    {
        alert("You tried to fool me? joke on you!! Plaese enter a real podcast name");
    }
}

/**
 * this function is an ajax GET call to Server to show all Transcripts
 * **/
function handleSubmitAllTranscripts() {
        var loader = document.getElementById("loader");
        document.getElementById("SearchBox").value = "";
        loader.style.display = "";
        $.ajax({
            type: "GET",
            contentType: 'application/json',
            //contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
            url: "/PodcastsSpeechToText/ShowAllTranscripts",
            success: function (response) {
                loader.style.display = "none";
                AllTrascribes = response;
                AllTranscribeHandle(response);
            },
            error: function (response) {
                loader.style.display = "none";
                window.alert("Something Went Wrong : " + response.status + " " + response.statusText);
            }
    });
}


/**
 * function to send to the Server the data of the chosen podcast episode that the user want to Transcription.
 *
 *
 *send the episode and podcast data
 * */
function HandleTextIT(Mp3Url, Episode) {
    var StartTranscribe = window.confirm("Do you want to Transcribe this episode of " + Episode);
    var loader = document.getElementById("loader");
    var lang = "en-US";
    loader.style.display = "";
    if (StartTranscribe) {
        if (checkForHebrewCharacters(Episode)) {
            lang = "he-IL";
        }
        window.alert("The monkeys start to transcribe");
        var reqData = {
            "PodcastURL": Mp3Url,
            "EpisodeName": Episode,
            "PodcastInfo": ChoosenPodacast,
            "EpisodeInfo": ChoosenEpisode,
            "Lang":lang
        };
        $.ajax({
            type: "GET",
            contentType: 'application/json;application/x-www-form-urlencoded; charset=UTF-8',
            url: "/PodcastsSpeechToText/AudioToTextServlet",
            data: reqData,
            success: function (response) {
                loader.style.display = "none";
                if (response) {
                    window.alert("there is already a transcript to this podcast...");
                }
                else {
                    window.alert("Yay we Transcript the podcast successfully!!");
                }
            },
            error: function (response) {
                loader.style.display = "none";
                window.alert("Something Went Wrong : " + response.status + " " + response.statusText);
            }
        });
        console.log("To Text " + Mp3Url);
    }
}


/**
 * function to send to the Server the data of the chosen podcast.
 *
 * will return list of episodes of podcast
 *
 *send the RSS URL.
 * */
function HandleImgClick(URL) {
    var loader = document.getElementById("loader");
    loader.style.display = "";
    var reqData = {"data": URL};
    $.ajax({
        type: "GET",
        contentType: 'application/json;application/x-www-form-urlencoded; charset=UTF-8',
        url: "/PodcastsSpeechToText/PodcastEpisodes",
        data: reqData,
        success: function (response) {
            loader.style.display = "none";
            HandleResponseRss(response);
        },
        error: function (response) {
            loader.style.display = "none";
            window.alert("Something Went Wrong : " + response.status + " " + response.statusText);
        }
    });
}

/**
 * function to handle the response from the server of all transcribes
 *
 *will create pages of transcribes
 *
 * */
function AllTranscribeHandle(response) {
    RemoveElemetnsFromContainers("PodcastsList");
    RemoveElemetnsFromContainers("podcastUL");
    RemoveElemetnsFromContainers("Episodes");
    RemoveElemetnsFromContainers("pages");
    var listDiv = document.getElementById('AllTranscribes');
    var ul = document.createElement('ul');
    ul.setAttribute("id", "podcastUL");
    if (response.length == 0) {
        window.alert("Our finest monkeys tried to searched for what you asked, and didn't find anything in our data... Try something else");
    }
    else {
        for (var i = 0; i < response.length; ++i) {
            var h4 = document.createElement('h4');
            var img = document.createElement('img');
            var div = document.createElement('div');
            var divM = document.createElement('div');
            var Play = document.createElement('a'); //Play button
            var PlayIcon = document.createElement('span');
            var PodcastName = document.createElement('h5');//Podcast Name
            var EpisodeName = document.createElement('h5');//Episode Name
            var TranscribeIcon = document.createElement('span');
            var Transcribe = document.createElement('a'); //Play button
            var HitScore = document.createElement('h5');
            var HideTranscribeIcon = document.createElement('span');
            var HideTranscribe = document.createElement('a'); //Play button
            var TranscribeText = response[i].Text;

            h4.innerHTML = response[i].Artist;   // Use innerHTML to set the text
            HitScore.innerText = "Match Score : " + response[i].NumSearchHits;
            img.setAttribute("src", response[i].ArtworkURL);
            img.setAttribute("class", "align-self-center mr-3");
            img.setAttribute("width", "50");
            img.setAttribute("height", "50");

            PlayIcon.className = "glyphicon glyphicon-play";
            Play.setAttribute("class", " btn btn-primary  ml-2");
            Play.setAttribute("href", response[i].PodcastURL);
            Play.setAttribute("target", "_blank");
            Play.appendChild(PlayIcon);

            TranscribeIcon.className = "glyphicon glyphicon-eye-open";
            Transcribe.setAttribute("class", " btn btn-primary  ml-2");
            Transcribe.addEventListener('click', (function (TranscribeText) {
                return function () {
                    ShowTranscribe(TranscribeText);
                }
            })(TranscribeText));
            Transcribe.appendChild(TranscribeIcon);

            HideTranscribeIcon.className = "glyphicon glyphicon-eye-close";
            HideTranscribe.setAttribute("class", " btn btn-primary  ml-2");
            HideTranscribe.onclick = function (ev) { RemoveElemetnsFromContainers("Episodes"); };
            HideTranscribe.appendChild(HideTranscribeIcon);

            PodcastName.innerHTML = response[i].PodcastName;   // Use innerHTML to set the text
            EpisodeName.innerHTML = response[i].EpisodeName;

            divM.setAttribute("class", "media-body");
            div.setAttribute("class", "media border p-2");

            div.appendChild(img);
            divM.appendChild(h4);
            divM.appendChild(PodcastName);
            divM.appendChild(EpisodeName);
            divM.appendChild(HitScore);
            divM.appendChild(Play);
            divM.appendChild(Transcribe);
            divM.appendChild(HideTranscribe);
            div.appendChild(divM);
            ul.appendChild(div);
        }
        listDiv.appendChild(ul);
    }
    createPages();
}

/**
 * function to handle the response from Server.
 *
 * will create a list of Transcription podcasts to show to user.
 *
 * */
function luceneSearchResponse(response) {
    RemoveElemetnsFromContainers("PodcastsList");
    RemoveElemetnsFromContainers("Episodes");
    RemoveElemetnsFromContainers("podcastUL");
    RemoveElemetnsFromContainers("pages");
    var listDiv = document.getElementById('AllTranscribes');
    var ul = document.createElement('ul');
    var ResultCount = document.getElementById("ResultCount").value;
    ul.setAttribute("id", "podcastUL");
    if (response.length == 0) {
        window.alert("Our finest monkeys tried to searched for what you asked, and didn't find anything in our data... Try something else");
    }
    else {
        for (var i = 0; i < response.length; ++i) {
            var h4 = document.createElement('h4');
            var img = document.createElement('img');
            var div = document.createElement('div');
            var divM = document.createElement('div');
            var Play = document.createElement('a'); //Play button
            var PlayIcon = document.createElement('span');
            var PodcastName = document.createElement('h5');//Podcast Name
            var EpisodeName = document.createElement('h5');//Episode Name
            var TranscribeIcon = document.createElement('span');
            var Transcribe = document.createElement('a'); //Play button
            var HitScore = document.createElement('h5');
            var HideTranscribeIcon = document.createElement('span');
            var HideTranscribe = document.createElement('a'); //Play button
            var TranscribeText = response[i].Text;

            h4.innerHTML = response[i].Artist;   // Use innerHTML to set the text
            HitScore.innerText = "Match Score : " + response[i].NumSearchHits;
            img.setAttribute("src", response[i].ArtworkURL);
            img.setAttribute("class", "align-self-center mr-3");
            img.setAttribute("width", "50");
            img.setAttribute("height", "50");

            PlayIcon.className = "glyphicon glyphicon-play";
            Play.setAttribute("class", " btn btn-primary  ml-2");
            Play.setAttribute("href", response[i].PodcastURL);
            Play.setAttribute("target", "_blank");
            Play.appendChild(PlayIcon);

            TranscribeIcon.className = "glyphicon glyphicon-eye-open";
            Transcribe.setAttribute("class", " btn btn-primary  ml-2");
            Transcribe.addEventListener('click', (function (TranscribeText) {
                return function () {
                    ShowTranscribe(TranscribeText);
                }
            })(TranscribeText));
            Transcribe.appendChild(TranscribeIcon);

            HideTranscribeIcon.className = "glyphicon glyphicon-eye-close";
            HideTranscribe.setAttribute("class", " btn btn-primary  ml-2");
            HideTranscribe.onclick = function (ev) { RemoveElemetnsFromContainers("Episodes"); };
            HideTranscribe.appendChild(HideTranscribeIcon);

            PodcastName.innerHTML = response[i].PodcastName;   // Use innerHTML to set the text
            EpisodeName.innerHTML = response[i].EpisodeName;

            divM.setAttribute("class", "media-body");
            div.setAttribute("class", "media border p-2");

            div.appendChild(img);
            divM.appendChild(h4);
            divM.appendChild(PodcastName);
            divM.appendChild(EpisodeName);
            divM.appendChild(HitScore);
            divM.appendChild(Play);
            divM.appendChild(Transcribe);
            divM.appendChild(HideTranscribe);
            div.appendChild(divM);
            if (i < ResultCount) {
                div.style.display = "";
            }
            else {
                div.style.display = "none";
            }
            ul.appendChild(div);
        }
        listDiv.appendChild(ul);
    }
}


/**
 * function to handle the response from Server.
 *
 * will create a list of podcasts to show to user.
 *
 * */
function HandleResponse(response) {
    RemoveElemetnsFromContainers("PodcastsList");
    RemoveElemetnsFromContainers("podcastUL");
    RemoveElemetnsFromContainers("Episodes");
    RemoveElemetnsFromContainers("pages");
    var listDiv = document.getElementById('PodcastsList');
    var ResultCount = document.getElementById("ResultCount").value;
    var ul = document.createElement('ul');
    ul.setAttribute("id", "podcastUL");
    // if (response.resultCount < ResultCount)
    //     ResultCount = response.resultCount;
    if (response.resultCount == 0) {
        window.alert("We searched all over the web and got.... NOTHING... Sorry try again");
    }
    else {
        for (var i = 0; i < response.resultCount; ++i) {
            var h4 = document.createElement('h4');
            var img = document.createElement('img');
            var div = document.createElement('div');
            var feedURL;
            div.setAttribute("class", "media border p-2");
            h4.setAttribute("class", "mt-4 ml-3");
            h4.innerHTML = response.results[i].trackName;   // Use innerHTML to set the track name
            img.setAttribute("src", response.results[i].artworkUrl100);
            img.setAttribute("class", "mr-1 mt-1 rounded-circle");
            img.setAttribute("width", "50");
            img.setAttribute("height", "50");
            feedURL = response.results[i].feedUrl;
            div.addEventListener("click", (function (feedURL, i) {
                return function () {
                    ChoosenPodacast = response.results[i];
                    HandleImgClick(feedURL);
                }
            })(feedURL, i));
            div.appendChild(img);
            div.appendChild(h4);
            div.setAttribute("id","Podcast");
            if (i < ResultCount) {
                div.style.display = "";
            }
            else {
                div.style.display = "none";
            }
            ul.appendChild(div);
        }
        listDiv.appendChild(ul);
    }
}


/**
 * function to handle the response from Server.
 *
 * will create a list of podcasts episodes to show to user.
 *
 * */
function HandleResponseRss(response) {
    RemoveElemetnsFromContainers("Episodes");
    RemoveElemetnsFromContainers("pages");
    var listDiv = document.getElementById('Episodes');
    var ResultCount = document.getElementById("ResultCount").value;
    var ul = document.createElement('ul');
    ul.setAttribute("id", "episodesUL");
    // if (response.results.length < ResultCount)
    //     ResultCount = response.results.length;
    if (response.results.length == 0) {
        window.alert("We searched all over the web and got.... NOTHING... Sorry try again");
    }
    else {
        for (var i = 0; i < response.results.length; ++i) {
            var Mp3Url = response.results[i].PodCastURL;
            var episode = response.results[i].EpisodeName;
            var EpisodeLength = response.results[i].Duration;
            var mp3Link = document.createElement('h5');//mp3 link
            var STT = document.createElement('button');//Speech to text button
            var STTIcon = document.createElement('span');
            var length = document.createElement('button'); //length fo podcast
            var div = document.createElement('div'); //div to hold all of them
            var divM = document.createElement('div');
            var Play = document.createElement('a'); //Play button
            var PlayIcon = document.createElement('span');

            PlayIcon.className = "glyphicon glyphicon-play";
            Play.setAttribute("class", " btn btn-primary  ml-2");
            Play.setAttribute("href", response.results[i].PodCastURL);
            Play.setAttribute("target", "_blank");
            Play.appendChild(PlayIcon);

            STT.setAttribute("class", "btn btn-primary");
            STTIcon.className = "glyphicon glyphicon-list-alt";
            STT.appendChild(STTIcon);


            divM.setAttribute("class", "media-body");
            div.setAttribute("class", "media border p-3");

            STT.innerHTML = "Text IT";
            STT.setAttribute("class", "btn btn-primary");

            length.innerHTML = EpisodeLength;
            length.setAttribute("class", "btn btn-primary ml-2 disabled");

            STT.addEventListener('click', (function (Mp3Url, episode, i) {
                return function () {
                    ChoosenEpisode = response.results[i];
                    HandleTextIT(Mp3Url, episode);
                }
            })(Mp3Url, episode, i));
            mp3Link.innerHTML = response.results[i].EpisodeName;   // Use innerHTML to set the text
            divM.appendChild(mp3Link);
            divM.appendChild(STT);
            divM.appendChild(length);
            divM.appendChild(Play);
            div.appendChild(divM);
            if (i < ResultCount) {
                div.style.display = "";
            }
            else {
                div.style.display = "none";
            }
            ul.appendChild(div);
        }
        listDiv.appendChild(ul);
    }
}


/**
 *
 * this function is to make the search result look dynamic
 * */

function filterResults() {
    var podcastUL, podcastLI, episodesUL, episodesLI, i, alsoEpisodes = 0;
    var ResultCount = document.getElementById("ResultCount").value;
    var AllTranscribs = document.getElementById("pages");
    var liTranscribs = AllTranscribs.getElementsByTagName("li");
    podcastUL = document.getElementById("podcastUL");
    if (podcastUL != null) {
        podcastLI = podcastUL.getElementsByClassName("media border p-2");
        episodesUL = document.getElementById("episodesUL");
        if (episodesUL != null) {
            episodesLI = episodesUL.getElementsByClassName("media border p-3");
            alsoEpisodes = 1;
        }
        for (i = 0; i < podcastLI.length; i++) {
            if (i < ResultCount) {
                podcastLI[i].style.display = "";
                if (alsoEpisodes == 1 && i < episodesLI.length)
                    episodesLI[i].style.display = "";

            }
            else {
                podcastLI[i].style.display = "none";
                if (alsoEpisodes == 1 && i < episodesLI.length)
                    episodesLI[i].style.display = "none";
            }
        }
    }
    if (liTranscribs != null)
    {
        createPages();
    }
}

/**
 * this function is for showing the Transcribe of given podcast episode.
 * */
function ShowTranscribe(Transcribe) {
    RemoveElemetnsFromContainers("Episodes");
    var searchTerm = document.getElementById("SearchBox").value;
    var Episodes = document.getElementById('Episodes');
    var div = document.createElement('div');
    var p = document.createElement('p');

    p.setAttribute("id","transcribe");
    div.setAttribute("class", "media border p-3 m-4");
    p.innerText = Transcribe;
    div.appendChild(p);
    Episodes.appendChild(div);
    $("#transcribe").mark(searchTerm);
}

/**
 * function to remove all the dom element of given container
 *
 **/
function RemoveElemetnsFromContainers(ContainerName) {
    var Elem = document.getElementById(ContainerName);
    while (Elem != null && Elem.firstChild) {
        Elem.removeChild(Elem.firstChild);
    }
    if(Elem != null && ContainerName == "podcastUL")
    {
        Elem.parentNode.removeChild(Elem);
    }
}


/**
 * this function is for creating paging to all transcribe.
 *
 * */
function createPages()
{
    RemoveElemetnsFromContainers("pages");
    var divider = document.getElementById("ResultCount").value;
    var pagesNav = document.getElementById("pages");
    var pages = AllTrascribes.length / divider;
    for (var i=0;i<pages;i++)
    {
        var li = document.createElement('li');
        var a = document.createElement('a');
        li.setAttribute("class","page-item");
        a.setAttribute("class","page-link");
        a.innerHTML= i;
        a.addEventListener('click', (function (i) {
            return function () {
                ChangeShowTranscribes(i);
            }
        })(i));
        li.appendChild(a);
        pagesNav.appendChild(li);
    }
    ChangeShowTranscribes(0);
}

/**
 * this function is to control the flow of showen transcribes
 * */
function ChangeShowTranscribes(index)
{
    RemoveElemetnsFromContainers("Episodes");
    var divider = document.getElementById("ResultCount").value;
    var transcribs = document.getElementById("AllTranscribes");
    var list = transcribs.getElementsByClassName("media border p-2");
    for(i=0;i<list.length;i++)
    {
        if(i >= index * divider && i<((index * divider) + +divider))
        {
            list[i].style.display = "";
        }
        else
        {
            list[i].style.display = "none";
        }
    }

}

/**
 * function to check if user enter a Hebrew words
 *
 * */
function checkForHebrewCharacters(value) {
    var patt = new RegExp("[\\u0590-\\u05FF]*");
    return patt.test(value);
}


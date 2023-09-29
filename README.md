# Monitoring tool with spring-boot

    configuration is avalable under resources folder in config.yml file
    
    example below:
    
    monitor:  
        interval: 1000 #ms // every 1 second by default
        url: "http://httpstat.us/200" # the remote host to monitor
        window_sizes:
            - 10 #second
            - 60 #seconds
            - 3600 #seconds 


# Getting Started
    mvn clean install

    mvn spring-boot:run


# Visiting monitoring webpage

    http://localhost:8080/index.html

![screenshot](https://raw.githubusercontent.com/metao1/monitor-tool-spring-boot/main/scsch.png)

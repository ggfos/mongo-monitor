mongo-monitor
=============

### description
    mongo monitor for scala ,when mongodb servers downtime,it can send email to the subscribers,
    hrough the properties.conf file to configure some boot parameters
    Through the a file to configure some boot parameters

### configure
    #properties.conf
    monitor{
        server{
                interval = 60
                debug = true
                address = 10.0.1.39:30000
                recipients = "primos@qq.com"
                db = "test"
                collection = "test"
        }
        mail{
            host = "smtp.yeah.net"
            username = "primos@yeah.net"
            password = "primostest"
            mimeType = "text/html;charset=GBK"
        }
    }
> interval : interval time seconds .eg 100

  debug : output log .eg true

  address : mongos config shard addresses .eg 192.168.1.9:20000

  recipients : the mail recipients .eg primos@qq.com,wangchunlei@gmail.com

  db : mongodb db .eg test

  collection : mongodb collection .eg test

  host : email smtp host

  username : email username

  password : email password

  mimeType : email content mimeType



### build
> cd mongo-monitor

  sbt compile pack

### run
> cd target/pack/bin

  sh mongo-monitor

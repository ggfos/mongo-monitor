mongo-monitor
=============

### description
    mongo monitor for scala ,when mongodb servers downtime,it can send email to the subscribers,<br />
    hrough the properties.conf file to configure some boot parameters<br />
    Through the a file to configure some boot parameters<br />

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
> interval : interval time seconds .eg 100<br />
  debug : output log .eg true<br />
  address : mongos config shard addresses .eg 192.168.1.9:20000<br />
  recipients : the mail recipients .eg primos@qq.com,wangchunlei@gmail.com<br />
  db : mongodb db .eg test<br />
  collection : mongodb collection .eg test<br />
  host : email smtp host<br />
  username : email username<br />
  password : email password<br />
  mimeType : email content mimeType<br />



### build
> cd mongo-monitor<br />
  sbt compile pack

### run
> cd target/pack/bin<br />
  sh mongo-monitor

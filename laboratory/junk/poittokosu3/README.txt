
■「ぽいっと工数」について
マツダ本部伝統のExcel工数管理表「ぱっと工数管理」を、ぽいっと作成するためのツールです。


■実行方法
1.run.batをぽち
2.コンソールが開き、年度と月が聞かれるので回答
3.「ぱっと工数管理」が完成


■設定
config/poittokosu.propertiesで変更します


■使い方
使い方は2通りあります

・使い方1（Easyな使い方）
曜日ごとのテンプレートを読み込んで作成する方法です

1.Excelのtemplateシートに、曜日ごとのテンプレートを記入
2.run.batをぽち

・使い方2（高度な使い方）
曜日ごとのテンプレートを読み込んだ上に、さらにGoogleCalendarAPIと連携して作成してやろうという方法です
両者の予定が被った場合は、GoogleCalendarの予定が優先されます。（<< マージ方法が難しかった。bugがあったらすみません...）

1.Google Calendar APIの利用設定をON（詳細は以下）
2.Google認証ファイル（client_secret.json）を、config直下に配置
3.poittokosu.propertiesより、poitto.useGoogleCalendarAPI=trueに設定
4.run.batをぽち


■Google Calendar APIの利用方法
1.Googleアカウントにて、プロジェクトを作成
2.作成したプロジェクトにて、Google Calendar APIの利用を「有効」にする
3.OAuth Client IDを作成してダウンロード
(client_secret_XXXXX.jsonとなっているので、client_secret.jsonにRename << GoogleDocにそうしろと書いてある)


■利用ライブラリ
・Spring
・Poi
・Logback
・GoogleAPI

# Store Apps - MVVM Clean Architecture (under development)

- Modularization
- UseCase pattern
- Retrofit
- Moshi
- Dagger Hilt Dependencies Injection
- Kotlin flow (with stateFlow experimental)
- Paging 3
- Navigation Component
- Data Store Preferences
- Room
- Work Manager
- Download Manager
- ViewBinding (with helper by [yogacp](https://github.com/yogacp/android-viewbinding))

|Home|Detail|Detail downloading|Download monitor|
|--|--|--|--|
|![](https://raw.githubusercontent.com/utsmannn/store-apps-mvvm-clean-architecture/master/images/home.png)|![](https://raw.githubusercontent.com/utsmannn/store-apps-mvvm-clean-architecture/master/images/detail.png)|![](https://raw.githubusercontent.com/utsmannn/store-apps-mvvm-clean-architecture/master/images/detail_download.png)|![](https://raw.githubusercontent.com/utsmannn/store-apps-mvvm-clean-architecture/master/images/download_monitor.png)|

|Download manager|Auto installer|Options auto installer|App updated|
|--|--|--|--|
|![](https://raw.githubusercontent.com/utsmannn/store-apps-mvvm-clean-architecture/master/images/download_manager.png)|![](https://raw.githubusercontent.com/utsmannn/store-apps-mvvm-clean-architecture/master/images/installer.png)|![](https://raw.githubusercontent.com/utsmannn/store-apps-mvvm-clean-architecture/master/images/options_installer.png)|![](https://raw.githubusercontent.com/utsmannn/store-apps-mvvm-clean-architecture/master/images/updated.png)|

---

## Stream Data Flow
![](https://raw.githubusercontent.com/utsmannn/store-apps-mvvm-clean-architecture/master/images/stream_data_flow.png)

- **Network**: Data berasal dari API Aptoide dan dieksekusi pada network threading menggunakan Retrofit.
- **Repository** : Route retrofit berasal dari api service yang dipanggil oleh repository dengan fungsi interactor flow yang menghasilkan response yang telah wrap oleh class result.
- **Domain** : Result yang berisi state, loading, success atau error di convert ke dalam DTO yang bertindak sebagai data object yang akan diparsing ke dalam view. Result yang berisi state dari data object tersebut menjadi flow variable.
- **ViewModel** : Dalam viewmodel, flow variable tersebut di convert menjadi LiveData dengan ViewModelScope agar bisa dibinding oleh lifecycle activity atau fragment untuk dapat diteruskan menuju view.
- **View** : LiveData yang berisi result di ekstrak dalam state idle, loading, success dan error. Dimana state success akan menghasilkan data yang dapat diparsing dalam view, sementara error akan menghasilkan throwable.

---

## Modularization
Dalam proyek ini, saya memisahkan fitur dan layer secara modular sehingga mudah untuk menjaga kode dan tanggungjawab setiap class pada layer masing-masing dan ketergantungannya.

tree:
```
root
--- app
--- data
--- feature
------ detail
------ home
------ listing
--- libraries
------ abstraction
------ network
```

### Apa itu layer?
Layer yang dimaksud dalam prinsip Clean Arch adalah lapisan-lapisan yang mewakili fungsionalitas dari class-class yang membangun sebuah fitur. Untuk gampang nya bisa lihat gambar berikut.

![](https://miro.medium.com/max/942/1*Jve_0_GCxLEiYzdc2QKogQ.jpeg)

Lingkaran tersebut berisi macam-macam lapisan atau layer. Layer-layer itulah yang membangun sebuah fitur dengan scope tugas masing-masing.

- **Presentation** : Ini adalah layer UI, tempat user berinteraksi, akhir dari semua aliran data (stream data). Activity atau fragment yang berisi view-view ada dalam layer ini
- **UseCase** : Layer ini berisi fungsi-fungsi tindakan user yang berasal dari view, inti dari bisnis logic berawal dari sini
- **Data** : Berisi semua sumber data. Stream data berawal dari sini kemudian diteruskan oleh bisnis logic class.
- **Domain** : Berisi logika bisnis dari sebuah fitur, biasa nya layer ini juga berisi UseCase class.
- **External** : Layer diluar bisnis logic dan view, biasa nya berisi abstraction dan helper.

## UseCase Pattern
UseCase dalam arsitektur ini menjadi jembatan bagi view dan data, satu level dengan Domain layer. Berisi fungsi-fungsi yang dapat dieksekusi atau diteruskan ke Data layer dan bergantung pada user input Presentasion layer. Dalam project ini, UseCase menghasilkan data yang dibungkus oleh ```ResultState```


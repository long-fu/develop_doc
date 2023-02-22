# 错误集锦

## iOS保存照片 

**Error Domain=PHPhotosErrorDomain Code=3300 "(null)"**

```swift
static func writeLivePhoto2Album(_ photo: Data, liveData: Data, completionHandler: @escaping ((Bool, Error?) -> Void)) {
                
//        var path =  HFileManager.shared.downloadTemp
//        try! FileManager.default.createDirectory(at: path, withIntermediateDirectories: true)
//        path.appendPathComponent("test")
//        let image = path.appendingPathExtension("jpg")
//        let mov = path.appendingPathExtension("mov")
//        debugPrint(image)
//
//        try! photo.write(to: image)
//        try! liveData.write(to: mov)
        
        PHPhotoLibrary.shared().performChanges({

            let options = PHAssetResourceCreationOptions()
            let request = PHAssetCreationRequest.forAsset()
            
            // 这是对的 苹果给了接口但是不能直接保存data数据 垃圾苹果
            request.addResource(with: .photo, fileURL: image, options: nil)
            request.addResource(with: .pairedVideo, fileURL: mov, options: nil)
            // "写入照片结果" false Optional(Error Domain=PHPhotosErrorDomain Code=3300 "(null)")
            //request.addResource(with: .photo, data: photo, options: nil)
            //request.addResource(with: .pairedVideo, data: liveData, options: nil)
            
        },completionHandler: completionHandler)
    }
```
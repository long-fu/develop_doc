const fs = require('fs');

main()

function main() {
    srcDir = "/home/haoshuai/code/develop_doc/minio"
    let allFiles = getAllFiles(srcDir,"test","minio/");
    // console.log(`文件数量:${allFiles.length}`);
    // for (let i = 0; i < allFiles.length; i++) {
    //     // console.log(allFiles[i]);
    //     // 同步读取文件内容
    //     // let content = fs.readFileSync(filePath).toString();
    //     // console.log(content);
	// }
}

/**
 * 递归遍历，获取指定文件夹下面的所有文件路径
 */
function getAllFiles(filePath,bucketName, objectName) {
    let allFilePaths = [];
    if (fs.existsSync(filePath)) {
        const files = fs.readdirSync(filePath);
        for (let i = 0; i < files.length; i++) {
            let file = files[i]; // 文件名称（不包含文件路径）
            let currentFilePath = filePath + '/' + file;
            
            let stats = fs.lstatSync(currentFilePath);
            if (stats.isDirectory()) {
                // console.log(file)
                getAllFiles(currentFilePath, bucketName, objectName + file + "/")
                // allFilePaths = allFilePaths.concat(getAllFiles(currentFilePath,bucketName,objectName + file + "/"));
            } else {
                temp = objectName + file
                console.log(currentFilePath,temp,bucketName)
            //    allFilePaths.push(currentFilePath);
               // 上传路径
            }
        }
    } else {
        console.warn(`指定的目录${filePath}不存在！`);
    }

    return [];
}

// isfotadmin
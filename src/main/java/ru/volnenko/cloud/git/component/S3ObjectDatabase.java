package ru.volnenko.cloud.git.component;

import io.minio.MinioClient;
import io.minio.Result;
import io.minio.messages.Item;
import lombok.Cleanup;
import lombok.NonNull;
import lombok.SneakyThrows;
import org.eclipse.jgit.internal.storage.dfs.*;
import org.eclipse.jgit.internal.storage.pack.PackExt;
import ru.volnenko.cloud.git.util.CryptUtil;
import ru.volnenko.cloud.git.util.DataUtil;
import ru.volnenko.cloud.git.util.SettingUtil;

import java.io.*;
import java.util.*;

import static ru.volnenko.cloud.git.util.StringUtil.removeSuffixIfExists;

public final class S3ObjectDatabase extends DfsObjDatabase {

    @NonNull
    private static final String TYPE = "application/octet-stream";

    @NonNull
    private final MinioClient minioClient;
    
    private final String bucketName = SettingUtil.getS3Bucket();

    @NonNull
    private final RepositoryInitializer repositoryInitializer;

    public S3ObjectDatabase(
            @NonNull final DfsRepository repository,
            @NonNull final DfsReaderOptions options,
            @NonNull final MinioClient minioClient,
            @NonNull final RepositoryInitializer repositoryInitializer
    ) {
        super(repository, options);
        this.minioClient = minioClient;
        this.repositoryInitializer = repositoryInitializer;
    }

    @SneakyThrows
    private synchronized List<DfsPackDescription> getFiles() {
        @NonNull final Map<String, S3Pack> map = new LinkedHashMap<>();
        @NonNull final String repo = this.getRepository().getDescription().getRepositoryName() + ".git/objects/pack/";
        @NonNull final Iterable<Result<Item>> items = minioClient.listObjects(bucketName, repo);

        for (@NonNull final Result<Item> item: items) {
            @NonNull final String name = item.get().objectName();
            @NonNull final String[] parts = name.split("/");
            @NonNull String filename = parts[parts.length - 1];
            if (filename.endsWith(".idx")) filename = removeSuffixIfExists(filename, ".idx");
            if (filename.endsWith(".pack")) filename = removeSuffixIfExists(filename, ".pack");

            PackSource packSource = null;
            PackExt packExt = null;
            if (filename.contains("insert")) packSource = PackSource.INSERT;
            if (filename.contains("receive")) packSource = PackSource.RECEIVE;
            if (filename.contains("compact")) packSource = PackSource.COMPACT;
            if (name.endsWith("idx")) packExt = PackExt.INDEX;
            if (name.endsWith("pack")) packExt = PackExt.PACK;

            if (packSource == null) continue;
            if (PackExt.PACK.equals(packExt)) {
                map.put(filename,  new S3Pack(filename, this.getRepository().getDescription(), packSource));
            }
        }

        for (@NonNull final Result<Item> item: items) {
            @NonNull final String name = item.get().objectName();
            @NonNull final String[] parts = name.split("/");
            @NonNull String filename = parts[parts.length - 1];
            if (filename.endsWith(".idx")) filename = removeSuffixIfExists(filename, ".idx");
            if (filename.endsWith(".pack")) filename = removeSuffixIfExists(filename, ".pack");

            PackExt packExt = null;
            if (name.endsWith("idx")) packExt = PackExt.INDEX;
            if (name.endsWith("pack")) packExt = PackExt.PACK;

            final S3Pack pack = map.get(filename);
            if (pack == null || packExt == null) continue;

            @NonNull final InputStream stream = minioClient.getObject(bucketName, name);
            @NonNull final byte[] objectBytes = DataUtil.getBytes(stream);
            pack.put(packExt, objectBytes);
            pack.setFileSize(packExt, objectBytes.length);
        }

        return new ArrayList<>(map.values());
    }

    @SneakyThrows
    protected synchronized List<DfsPackDescription> listPacks() {
        @NonNull final Map<String, S3Pack> map = new LinkedHashMap<>();
        @NonNull final String repo = this.getRepository().getDescription().getRepositoryName() + ".git/";
        @NonNull final Iterable<Result<Item>> items = minioClient.listObjects(bucketName, repo);

        for (@NonNull final Result<Item> item: items) {
            final String name = item.get().objectName();
            final String[] parts = name.split("/");
            String filename = parts[parts.length - 1];
            if (filename.endsWith(".ref")) filename = removeSuffixIfExists(filename, ".ref");
            if (filename.endsWith(".idx")) filename = removeSuffixIfExists(filename, ".idx");
            if (filename.endsWith(".pack")) filename = removeSuffixIfExists(filename, ".pack");
            PackSource packSource = null;
            PackExt packExt = null;
            if (filename.contains("insert")) packSource = PackSource.INSERT;
            if (filename.contains("receive")) packSource = PackSource.RECEIVE;
            if (filename.contains("compact")) packSource = PackSource.COMPACT;
            if (name.endsWith("ref")) packExt = PackExt.REFTABLE;
            if (name.endsWith("idx")) packExt = PackExt.INDEX;
            if (name.endsWith("pack")) packExt = PackExt.PACK;
            if (PackExt.REFTABLE.equals(packExt)) packSource = PackSource.INSERT;
            if (PackExt.PACK.equals(packExt) || PackExt.REFTABLE.equals(packExt)) {
                map.put(filename,  new S3Pack(filename, this.getRepository().getDescription(), packSource));
            }
        }

        for (@NonNull final Result<Item> item: items) {
            @NonNull final String name = item.get().objectName();
            @NonNull final String[] parts = name.split("/");
            @NonNull String filename = parts[parts.length - 1];
            if (filename.endsWith(".ref")) filename = removeSuffixIfExists(filename, ".ref");
            if (filename.endsWith(".idx")) filename = removeSuffixIfExists(filename, ".idx");
            if (filename.endsWith(".pack")) filename = removeSuffixIfExists(filename, ".pack");
            PackSource packSource = null;
            PackExt packExt = null;
            if (filename.contains("insert")) packSource = PackSource.INSERT;
            if (filename.contains("receive")) packSource = PackSource.RECEIVE;
            if (filename.contains("compact")) packSource = PackSource.COMPACT;
            if (name.endsWith("ref")) packExt = PackExt.REFTABLE;
            if (name.endsWith("idx")) packExt = PackExt.INDEX;
            if (name.endsWith("pack")) packExt = PackExt.PACK;
            if (PackExt.REFTABLE.equals(packExt)) packSource = PackSource.INSERT;

            final S3Pack pack = map.get(filename);
            if (pack == null) continue;

            @NonNull @Cleanup final InputStream stream = minioClient.getObject(bucketName, name);
            @NonNull final byte[] objectBytes = DataUtil.getBytes(stream);
            pack.put(packExt, objectBytes);
            pack.setFileSize(packExt, objectBytes.length);
        }

       return new ArrayList<>(map.values());
    }

    @SneakyThrows
    protected DfsPackDescription newPack(DfsObjDatabase.PackSource source) {
        @NonNull final String id = "pack-"+ CryptUtil.toSHA1(UUID.randomUUID().toString()) +"-"+source.name().toLowerCase();
        return new S3Pack(id, this.getRepository().getDescription(), source);
    }

    protected synchronized void commitPackImpl(
            final Collection<DfsPackDescription> descriptions,
            final Collection<DfsPackDescription> replace
    ) {
        repositoryInitializer.init(getRepository().getDescription());
    }

    @SneakyThrows
    protected ReadableChannel openFile(
            @NonNull final DfsPackDescription desc,
            @NonNull final PackExt ext
    ) {
        @NonNull final S3Pack memPack = (S3Pack) desc;
        final byte[] file = memPack.get(ext);
        if (file == null) throw new FileNotFoundException(desc.getFileName(ext));
        return new ByteArrayReadableChannel(file, 4096);
    }

    @SneakyThrows
    protected DfsOutputStream writeFile(DfsPackDescription desc, final PackExt ext) {
        @NonNull final S3Pack memPack = (S3Pack) desc;
        return new S3DfsOutputStream() {
            @SneakyThrows
            public void flush() {
                @Cleanup @NonNull final ByteArrayInputStream inputStream = new ByteArrayInputStream(this.getData());
                @NonNull String path = "";
                if (PackExt.PACK.equals(ext)) path = "objects/pack/";
                if (PackExt.INDEX.equals(ext)) path = "objects/pack/";
                if (PackExt.REFTABLE.equals(ext)) path = "reftable/tables.list/";
                String prefix = "";
                @NonNull final String filename = prefix + desc.getFileName(ext);
                @NonNull final String repoName = memPack.getRepositoryDescription().getRepositoryName();
                @NonNull final String objectName = repoName + ".git/" + path + filename;
                minioClient.putObject(bucketName, objectName, inputStream, TYPE);
                memPack.put(ext, this.getData());
            }
        };
    }

    @NonNull
    @SneakyThrows
    public List<DfsPackDescription> refs() {
        @NonNull final List<DfsPackDescription> result = new ArrayList<>();
        @NonNull final String repo = this.getRepository().getDescription().getRepositoryName() + ".git/reftable/tables.list/";
        @NonNull final Iterable<Result<Item>> items = minioClient.listObjects(bucketName, repo);
        for (@NonNull final Result<Item> item: items) {
            @NonNull final String name = item.get().objectName();
            @NonNull final String[] parts = name.split("/");
            @NonNull String filename = parts[parts.length -1];
            if (filename.endsWith(".ref")) filename = removeSuffixIfExists(filename, ".ref");
            @NonNull final S3Pack memPack = new S3Pack(filename, this.getRepository().getDescription(), PackSource.INSERT);
            @NonNull @Cleanup final InputStream stream = minioClient.getObject(bucketName, name);
            @NonNull final byte[] objectBytes = DataUtil.getBytes(stream);
            memPack.put(PackExt.REFTABLE, objectBytes);
            memPack.setFileSize(PackExt.REFTABLE, objectBytes.length);
            memPack.setBlockSize(PackExt.REFTABLE, 4096);
            memPack.setMinUpdateIndex(1);
            result.add(memPack);
        }
        return result;
    }

    @NonNull
    public DfsPackFile[] files() {
        @NonNull final List<DfsPackFile> result = new ArrayList<>();
        for (@NonNull final DfsPackDescription item: getFiles()) {
            if (item.getPackSource().equals(PackSource.RECEIVE)) {
                result.add(new DfsPackFile(DfsBlockCache.getInstance(), item));
            }
        }
        return result.toArray(new DfsPackFile[]{});
    }

    @Override
    public DfsReftable[] getReftables() throws IOException {
        @NonNull final List<DfsReftable> reftables = new ArrayList<>();
        for (@NonNull final DfsPackDescription item: refs()) {
            reftables.add(new DfsReftable(item));
        }
        return reftables.toArray(new DfsReftable[]{});
    }

    @Override
    public PackList getPackList() throws IOException {
        return new PackList(files(), getReftables());
    }

    public long getApproximateObjectCount() {
        return 0;
    }

    @Override
    protected void rollbackPack(Collection<DfsPackDescription> collection) {
    }

}

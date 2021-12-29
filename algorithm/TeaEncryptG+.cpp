#include <string.h>
#include <stdio.h>
char *key = "troqkddmtroqkcdm";
char *postfix = "e8b10c1f8bc3595be8b10c1f8bc3595b";
class Encrypt {
	public:
	void getKeyPre(char sixkey[9],char *ret) {
		ret[0] = 0;
		char res1[80];
		encrypt(sixkey, res1);
		strcat(ret, res1);
		strcat(ret, postfix);
	}
	private:
	long long bytes_to_long(char *t) {
		unsigned char *r = (unsigned char *)t;
		long long a = (0xFFFFFFFF & (unsigned int)r[0]) << 24;
		long long b = (0xFFFFFFFF & (unsigned int)r[1]) << 16;
		long long c = (0xFFFFFFFF & (unsigned int)r[2]) << 8;
		long long d = r[3];
		return a + b + c + d;
	}
	void long_to_bytes(long long v, char *ret) {
		int a = (int)((0xFF000000 & v) >> 24);
		int b = (int)((0xFF0000 & v) >> 16);
		int c = (int)((0xFF00 & v) >> 8);
		int d = (int)(0xFF & v);
		sprintf(ret, "%02x%02x%02x%02x", a, b, c, d);
	}
	long long unpack(char* tmp,int start,int len) {
		unsigned char *arr = (unsigned char *)(tmp + start);
		long long d = arr[0];
		long long c = arr[1];
		long long b = arr[2];
		long long a = arr[3];
		a = a << 24;
		b = b << 16;
		c = c << 8;
		return a + b + c + d;
	}
	void encrypt(char v[9], char*ret) {
		long long v0 = bytes_to_long(v);
		long long v1 = bytes_to_long(v + 4);
		long long sum = 0;
		for (int i = 0; i < 32; ++i) {
			long long tv1 = toUInt32(toUInt32(v1 << 4)) ^ toUInt32((v1 >> 5 & 0x07FFFFFF));
			long long tv2 = unpack(key, ((int)sum & 3) * 4, 4);
			v0 = toUInt32(v0 + (toUInt32(tv1 + v1) ^ toUInt32(tv2 + sum)));
			sum = toUInt32(sum + 0x9E3779B9);
			tv1 = toUInt32(toUInt32(toUInt32(v0 << 4)) ^ toUInt32((v0 >> 5 & 0x07FFFFFF)));
			tv2 = unpack(key, ((int)toUInt32((int)sum >> 11) & 3) * 4, 4);
			v1 = toUInt32(v1 + (toUInt32(tv1 + v0) ^ toUInt32(tv2 + sum)));
		}
		long_to_bytes(v0, ret);
		long_to_bytes(v1, ret + 8);
	}
	long long toUInt32(long long v) {
		return v & 0xFFFFFFFF;
	}
}
;
void getKey(char sixkey[9],char *ret) {
	Encrypt enc;
	enc.getKeyPre(sixkey, ret);
}
int main() {
	char password[] = "uu5!^%jg";
	char res[100];
	getKey(password,res);
	printf("%s",res);
	return 0;
}